package com.edu.pe.pagaPeBackend.user.controller;


import com.edu.pe.pagaPeBackend.user.dto.users.UserDTO;
import com.edu.pe.pagaPeBackend.user.dto.users.UserMapper;
import com.edu.pe.pagaPeBackend.user.exception.ResourceNotFoundException;
import com.edu.pe.pagaPeBackend.user.exception.ValidationException;
import com.edu.pe.pagaPeBackend.user.model.Roles;
import com.edu.pe.pagaPeBackend.user.model.User;
import com.edu.pe.pagaPeBackend.user.repository.UserRepository;
import com.edu.pe.pagaPeBackend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200", "https://pagape-frontend.azurewebsites.net", "https://ambitious-water-0d42bc910.6.azurestaticapps.net/"})
@RestController
@RequestMapping("/api/pagaPe/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    private final UserRepository userRepository;


    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional(readOnly = true)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserDTO>> getAllNumbersByStatus(@PathVariable boolean status) {
        List<User> users = userService.getAllUsersByStatus(status);
        List<UserDTO> userDTOs = users.stream()
                .map(UserMapper::fromUser)  // Utiliza el método estático fromUser
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @Transactional(readOnly = true)
    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAllNumbers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(UserMapper::fromUser)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        // Extraer el userId del contexto de seguridad
        String email = authentication.getName(); // El filtro configura el userId como nombre de usuario

        // Buscar al usuario por su id
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Mapear el usuario a DTO y devolver la respuesta
        UserDTO userDTO = UserMapper.fromUser(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/check/dni/{dni}")
    public ResponseEntity<Map<String, Object>> checkDNIExists(@PathVariable String dni) {
        try {
            boolean exists = userService.checkDNIExists(dni);
            Map<String, Object> response = new HashMap<>();
            response.put("exists", exists);
            response.put("dni", dni);
            response.put("message", exists ? "DNI ya existe" : "DNI disponible");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al verificar DNI: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/check/email/{email}")
    public ResponseEntity<Map<String, Object>> checkEmailExists(@PathVariable String email) {
        try {
            boolean exists = userRepository.existsByUserEmail(email);
            Map<String, Object> response = new HashMap<>();
            response.put("exists", exists);
            response.put("email", email);
            response.put("message", exists ? "Email ya existe" : "Email disponible");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al verificar email: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/check/phone/{phone}")
    public ResponseEntity<Map<String, Object>> checkPhoneExists(@PathVariable String phone) {
        try {
            boolean exists = userRepository.existsByUserPhone(phone);
            Map<String, Object> response = new HashMap<>();
            response.put("exists", exists);
            response.put("phone", phone);
            response.put("message", exists ? "Teléfono ya existe" : "Teléfono disponible");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al verificar teléfono: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Endpoint temporal de debug - REMOVER después de solucionar el problema
    @GetMapping("/debug/validation")
    public ResponseEntity<Map<String, Object>> debugValidation(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String phone) {
        try {
            Map<String, Object> response = new HashMap<>();
            
            if (email != null) {
                response.put("email_raw", email);
                response.put("email_trimmed", email.trim());
                response.put("email_exists", userRepository.existsByUserEmail(email));
                response.put("email_exists_trimmed", userRepository.existsByUserEmail(email.trim()));
            }
            
            if (dni != null) {
                response.put("dni_raw", dni);
                response.put("dni_trimmed", dni.trim());
                response.put("dni_exists", userRepository.existsByUserDNI(dni));
                response.put("dni_exists_trimmed", userRepository.existsByUserDNI(dni.trim()));
            }
            
            if (phone != null) {
                response.put("phone_raw", phone);
                response.put("phone_trimmed", phone.trim());
                response.put("phone_exists", userRepository.existsByUserPhone(phone));
                response.put("phone_exists_trimmed", userRepository.existsByUserPhone(phone.trim()));
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error en debug: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Transactional
    @PostMapping("/nameAndEmail")
    public ResponseEntity<UserDTO> getUserByDNI( @RequestBody User userRequest) {

        User user = userService.getUserByNameAndDNI(userRequest.getUserFirstName(), userRequest.getUserLastName(), userRequest.getUserDNI());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Mapear el usuario a DTO y devolver la respuesta
        UserDTO userDTO = UserMapper.fromUser(user);
        return ResponseEntity.ok(userDTO);
    }
    // URL: http://localhost:8081/api/thebigfun/v1/users/{userId}
    // Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDTO response = UserMapper.fromUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // URL: http://localhost:8081/api/rumba/v1/users/{userId}/image
    // Method: GET
    @Transactional
    @GetMapping("/{userId}/image")
    public ResponseEntity<byte[]> displayImage(@PathVariable(name= "userId")Long userId)throws IOException, SQLException {
        User user = userService.getUserById(userId);
        byte[] imageBytes = null;
        imageBytes = user.getImage().getBytes(1,(int)user.getImage().length());

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
    }
    // URL: http://localhost:8081/api/rumba/v1/users/{userId}/addImage
    // Method: POST
    @Transactional
    @PostMapping("/{userId}/addImage")
    public ResponseEntity<UserDTO> addImageUser(@PathVariable Long userId,
                                                @RequestParam("image") MultipartFile imageFile) {
        try {
            // Converti MultipartFile a Blob
            Blob imageBlob = multipartFileToBlob(imageFile);
            if (imageBlob == null) {
                // Manejamos en el caso en que la conversión falle
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            User user = userService.getUserById(userId);
            if (user == null) {
                // Usuario no encontrado
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            user.setImage(imageBlob);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(convertToDto(updatedUser));
        } catch (Exception e) {
            // Manejar otras excepciones
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    private Blob multipartFileToBlob(MultipartFile file) throws IOException, SerialException, SQLException {
        byte[] bytes = file.getBytes();
        return new SerialBlob(bytes);
    }
    // Manejar el peso para agregar un imagen
    @ControllerAdvice
    public class FileUploadExceptionAdvice {

        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("No se puede cargar el archivo. Tamaño máximo permitido excedido.");
        }
    }

    // URL: http://localhost:8081/api/thebigfun/v1/users/{userId}
    // Method: PUT
    @Transactional
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable(name = "userId") Long userId, @RequestBody User user) {
        existsUserByUserId(userId);
        validateUser(user);
        user.setId(userId);

        User updateUser = ifDifferentOrEmptyUpdate(user);
        UserDTO userDTO = UserMapper.fromUser(updateUser);
        return ResponseEntity.ok(userDTO);

    }

    @Transactional
    @PutMapping("/update2/{userId}")
    public ResponseEntity<UserDTO> update2User(@PathVariable(name = "userId") Long userId, @RequestBody User userRequest) {
        existsUserByUserId(userId);

        // Obtener el usuario actual desde la base de datos
        User existingUser = userService.getUserById(userId);

        // Actualizar solo los campos que se envían en la solicitud
        User updatedUser = userService.updateUser2(existingUser, userRequest);

        // Convertir a DTO antes de responder
        UserDTO userDTO = UserMapper.fromUser(updatedUser);
        return ResponseEntity.ok(userDTO);
    }

    // URL: http://localhost:8081/api/rumba/v1/users/{userId}
    // Method: DELETE
    @Transactional
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") Long userId) {
        existsUserByUserId(userId);
        userService.deleteUser(userId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }



    @Transactional
    @PatchMapping("/{userId}")
    public ResponseEntity<Void> softDeleteUser(@PathVariable(name = "userId") Long userId) {
        existsUserByUserId(userId);
        userService.deactivateNumber(userId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
    // URL: http://localhost:8080/api/rumba/v1/users/{userId}/events
    // Method: LISTA eventos del usuario



    // Nuevo endpoint para obtener detalles de usuario, eventos y reservas


    private UserDTO convertToDto(User user) {

        return UserDTO.builder()
                .id(user.getId())
                .userFirstName(user.getUserFirstName())
                .userLastName(user.getUserLastName())
                .userEmail(user.getUserEmail())
                .userPhone(user.getUserPhone())
                .imageData(user.getImageData())
                .build();

    }
    private void validateUser(User user) {
        if (user.getUserFirstName() == null || user.getUserFirstName().isEmpty()) {
            throw new ValidationException("El nombre del usuario debe ser obligatorio");
        }
        if (user.getUserFirstName().length() > 50) {
            throw new ValidationException("El nombre del usuario no debe exceder los 50 caracteres");
        }
        if (user.getUserLastName() == null || user.getUserLastName().isEmpty()) {
            throw new ValidationException("El apellido del usuario debe ser obligatorio");
        }
        if (user.getUserLastName().length() > 50) {
            throw new ValidationException("El apellido del usuario no debe exceder los 50 caracteres");
        }
        /*if (user.getUserEmail() == null || user.getUserEmail().isEmpty()) {
            throw new ValidationException("El email del usuario debe ser obligatorio");
        }
        if (user.getUserEmail().length() > 50) {
            throw new ValidationException("El email del usuario no debe exceder los 50 caracteres");
        }
        //if (user.getUserPassword() == null || user.getUserPassword().isEmpty()) {
            throw new ValidationException("La contraseña del usuario debe ser obligatorio");
        }
        if (user.getUserPassword().length() > 100) {
            throw new ValidationException("La contraseña del usuario no debe exceder los 100 caracteres");
        }*/
    }

    private void existsUserByEmail(User user) {
        if (userRepository.existsByUserEmail(user.getUserEmail())) {
            throw new ValidationException("Ya existe un usuario con el email " + user.getUserEmail());
        }
    }

    private void existsUserByEmail(String email) {
        if (!userRepository.existsByUserEmail(email)) {
            throw new ResourceNotFoundException("No existe un usuario con el email " + email);
        }
    }
    
    private boolean isValidValue(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    private void validateDNIUpdate(String newDNI, Long userId) {
        if (isValidValue(newDNI)) {
            // Verificar si el DNI ya existe en otro usuario
            if (userRepository.existsByUserDNI(newDNI.trim())) {
                // Obtener el usuario actual para verificar si el DNI es el mismo
                User currentUser = userService.getUserById(userId);
                if (currentUser == null || !newDNI.trim().equals(currentUser.getUserDNI())) {
                    throw new ValidationException("Ya existe un usuario con el DNI " + newDNI);
                }
            }
        }
    }
    
    private void validatePhoneUpdate(String newPhone, Long userId) {
        if (isValidValue(newPhone)) {
            // Verificar si el teléfono ya existe en otro usuario
            if (userRepository.existsByUserPhone(newPhone.trim())) {
                // Obtener el usuario actual para verificar si el teléfono es el mismo
                User currentUser = userService.getUserById(userId);
                if (currentUser == null || !newPhone.trim().equals(currentUser.getUserPhone())) {
                    throw new ValidationException("Ya existe un usuario con el teléfono " + newPhone);
                }
            }
        }
    }

    private void existsUserByUserId(Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new ResourceNotFoundException("No existe un usuario con el id " + userId);
        }
    }

    private User ifDifferentOrEmptyUpdate(User user) {
        return userRepository.findById(user.getId()).map(userToUpdate -> {
            if (user.getUserFirstName() != null && !user.getUserFirstName().isEmpty() && !user.getUserFirstName().equals(userToUpdate.getUserFirstName())) {
                userToUpdate.setUserFirstName(user.getUserFirstName());
            }
            if (user.getUserLastName() != null && !user.getUserLastName().isEmpty() && !user.getUserLastName().equals(userToUpdate.getUserLastName())) {
                userToUpdate.setUserLastName(user.getUserLastName());
            }
            if (user.getUserEmail() != null && !user.getUserEmail().isEmpty() && !user.getUserEmail().equals(userToUpdate.getUserEmail())) {
                userToUpdate.setUserEmail(user.getUserEmail());
            }
            if (user.getUserPhone() != null && !user.getUserPhone().isEmpty() && !user.getUserPhone().equals(userToUpdate.getUserPhone())) {
                // Validar que el teléfono no exista en otro usuario
                validatePhoneUpdate(user.getUserPhone(), user.getId());
                userToUpdate.setUserPhone(user.getUserPhone());
            }
            if (user.getUserDNI() != null && !user.getUserDNI().isEmpty() && !user.getUserDNI().equals(userToUpdate.getUserDNI())) {
                // Validar que el DNI no exista en otro usuario
                validateDNIUpdate(user.getUserDNI(), user.getId());
                userToUpdate.setUserDNI(user.getUserDNI());
            }
            if (user.getImageData() != null && !user.getImageData().isEmpty() && !user.getImageData().equals(userToUpdate.getImageData())) {
                userToUpdate.setImageData(user.getImageData());
            }
           // Aquí agregamos la lógica para actualizar el rol si es diferente solo el admin puede cambiar
            if (user.getRole() != null && !user.getRole().equals(userToUpdate.getRole())) {
                userToUpdate.setRole(user.getRole());
            }
            if(user.getRole() == Roles.ADMIN) {
                userToUpdate.setActive(user.getActive());
            }
            return userService.updateUser(userToUpdate);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + user.getId()));
    }







}
