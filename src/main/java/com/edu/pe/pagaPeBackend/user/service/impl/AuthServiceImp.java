package com.edu.pe.pagaPeBackend.user.service.impl;

import com.edu.pe.pagaPeBackend.user.dto.AuthenticationResponse;
import com.edu.pe.pagaPeBackend.user.dto.LoginRequest;
import com.edu.pe.pagaPeBackend.user.dto.RegisterRequest;
import com.edu.pe.pagaPeBackend.user.exception.ValidationException;
import com.edu.pe.pagaPeBackend.user.model.Roles;
import com.edu.pe.pagaPeBackend.user.model.Token;
import com.edu.pe.pagaPeBackend.user.model.TokenType;
import com.edu.pe.pagaPeBackend.user.model.User;
import com.edu.pe.pagaPeBackend.user.repository.TokenRepository;
import com.edu.pe.pagaPeBackend.user.repository.UserRepository;
import com.edu.pe.pagaPeBackend.user.service.AuthService;
import com.edu.pe.pagaPeBackend.user.service.EmailService;
import com.edu.pe.pagaPeBackend.user.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.EnumSet;


@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private SecureRandom random = new SecureRandom();

    private void validateRole(Roles role) {
        // EnumSet de roles permitidos para autoasignarse
        EnumSet<Roles> allowedRoles = EnumSet.of(Roles.WORKER, Roles.ADMIN);

        if (!allowedRoles.contains(role)) {
            throw new ValidationException("Asignación de rol no permitida.");
        }
    }

    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        //Validar datos del request
        validateRegisterRequest(registerRequest);
        
        //Validar que el email no exista
        existsUserByEmail(registerRequest);
        
        //Validar que el DNI no exista
        existsUserByDNI(registerRequest);
        
        //Validar que el teléfono no exista
        existsUserByPhone(registerRequest);
        
        //Aqui vamos a  validar rol antes de asignarlo
        validateRole(registerRequest.getRole());
        // Generar una clave privada y dirección Ethereum


        var user = User.builder()
                .userFirstName(registerRequest.getUserFirstName())
                .userLastName(registerRequest.getUserLastName())
                .userEmail(registerRequest.getUserEmail())
                .userDNI(registerRequest.getUserDNI())
                .userPassword(passwordEncoder.encode(registerRequest.getUserPassword()))
                .userPhone(registerRequest.getUserPhone())
                .imageData(registerRequest.getImageData())
                //.role(Roles.USER)
                .role(registerRequest.getRole())//Usa el rol del request despues de validar
                .active(true)
                .build();


        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .user_id(user.getId())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }

@Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserEmail(),
                        loginRequest.getUserPassword()));
        var user = userRepository.findByUserEmail(loginRequest.getUserEmail()).get();



        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

    System.out.println("Antes de actualizar lastLogin: " + user.getLastLogin());
    user.setLastLogin(LocalDateTime.now());
    userRepository.save(user);
    System.out.println("Después de actualizar lastLogin: " + user.getLastLogin());


    return AuthenticationResponse.builder()
                .user_id(user.getId())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByUserEmail(userEmail).get();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }

    }


    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public String generateRecoveryCodeByEmail(String email) {
        User user = userRepository.findByUserEmail(email).get();
        if (user != null) {
            // Genera un número entre 1000 y 9999
            int code = 1000 + random.nextInt(9000);
            // Convierte el código a String
            String codeStr = String.valueOf(code);
            // Establece el código de recuperación en el usuario
            user.setRecoveryCode(codeStr);
            // Guarda el usuario con el nuevo código de recuperación
            userRepository.save(user);
            // Utiliza el servicio de correo electrónico para enviar el código
            emailService.enviarCodigoRecuperacion(email, codeStr);
            // Retorna el código de recuperación
            return codeStr;
        } else {
            // Lanza una excepción si no se encuentra el usuario
            throw new ValidationException("Usuario no encontrado con el email: " + email);
        }
    }

    public boolean resetPassword(String email, String codigo, String newPassword) {
        User user = userRepository.findByUserEmail(email).get();
        if (user != null && user.getRecoveryCode().equals(codigo)) {
            String encodedPassword = passwordEncoder.encode(newPassword); // Asegúrate de encriptar la contraseña
            user.setUserPassword(encodedPassword);
            user.setRecoveryCode(null); // Limpiar el código de recuperación
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }



    public void validateRegisterRequest(RegisterRequest registerRequest)
    {
        if(registerRequest.getUserFirstName()==null  ||
                registerRequest.getUserFirstName().isEmpty())
        {
            throw new ValidationException("El nombre del usuario debe ser obligatorio");
        }
        if(registerRequest.getUserFirstName().length()>50)
        {
            throw new ValidationException("El nombre del usuario no debe exceder los 50 caracteres");
        }
        if(registerRequest.getUserLastName()==null || registerRequest.getUserLastName().isEmpty())
        {
            throw new ValidationException("El apellido del usuario debe ser obligatorio");
        }
        if(registerRequest.getUserLastName().length()>50)
        {
            throw new ValidationException("El apellido del usuario no debe exceder los 50 caracteres");
        }
        if (registerRequest.getUserEmail() == null || registerRequest.getUserEmail().isEmpty()) {
            throw new ValidationException("El email del usuario debe ser obligatorio");
        }
        if (registerRequest.getUserEmail().length() > 50) {
            throw new ValidationException("El email del usuario no debe exceder los 50 caracteres");
        }
        if (registerRequest.getUserPassword() == null || registerRequest.getUserPassword().isEmpty()) {
            throw new ValidationException("La contraseña del usuario debe ser obligatorio");
        }
        if (registerRequest.getUserPassword().length() > 100) {
            throw new ValidationException("La contraseña del usuario no debe exceder los 100 caracteres");
        }
    }
    public void existsUserByEmail(RegisterRequest registerRequest) {
        if (userRepository.existsByUserEmail(registerRequest.getUserEmail())) {
            throw new ValidationException("Ya existe un usuario con el email " + registerRequest.getUserEmail());
        }
    }
    
    public void existsUserByDNI(RegisterRequest registerRequest) {
        if (registerRequest.getUserDNI() != null && !registerRequest.getUserDNI().isEmpty()) {
            if (userRepository.existsByUserDNI(registerRequest.getUserDNI())) {
                throw new ValidationException("Ya existe un usuario con el DNI " + registerRequest.getUserDNI());
            }
        }
    }
    
    public void existsUserByPhone(RegisterRequest registerRequest) {
        if (registerRequest.getUserPhone() != null && !registerRequest.getUserPhone().isEmpty()) {
            if (userRepository.existsByUserPhone(registerRequest.getUserPhone())) {
                throw new ValidationException("Ya existe un usuario con el teléfono " + registerRequest.getUserPhone());
            }
        }
    }
}
