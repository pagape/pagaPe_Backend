package com.edu.pe.pagaPeBackend.user.service.impl;


import com.edu.pe.pagaPeBackend.user.exception.ResourceNotFoundException;
import com.edu.pe.pagaPeBackend.user.model.User;
import com.edu.pe.pagaPeBackend.user.repository.UserRepository;
import com.edu.pe.pagaPeBackend.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public User createUser(User user) {


        return userRepository.save(user);
    }

//    public void activateFreeTrial(User user, int days) {
//        user.setTrialExpiryDate(LocalDate.now().plusDays(days));
//        userRepository.save(user);
//    }
    @Override
    public User getUserById(Long user_id) {
        return userRepository.findById(user_id).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
            return userRepository.findByUserEmail(email).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el email: " + email));

    }

    @Override
    public User getUserByNameAndDNI(String firstName, String lastName,String dni) {

        return userRepository.findByUserFirstNameAndUserLastNameAndUserDNI(firstName,lastName,dni).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el nombre: " + firstName+ "ni el dni: "+ dni));
    }

    @Override
    public boolean checkDNIExists(String dni) {
        return userRepository.existsByUserDNI(dni);
    }

    @Override

    public List<User> getAllUsersByStatus(boolean status) {



        return userRepository.findByActive(status);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser2 ( User existingUser, User userRequest) {

        if (userRequest.getUserFirstName() != null && !userRequest.getUserFirstName().isEmpty()) {
            existingUser.setUserFirstName(userRequest.getUserFirstName());
        }
        if (userRequest.getUserLastName() != null && !userRequest.getUserLastName().isEmpty()) {
            existingUser.setUserLastName(userRequest.getUserLastName());
        }
        if (userRequest.getUserEmail() != null && !userRequest.getUserEmail().isEmpty()) {
            existingUser.setUserEmail(userRequest.getUserEmail());
        }
        if (userRequest.getUserDNI() != null && !userRequest.getUserDNI().trim().isEmpty()) {
            // Validar que el DNI no exista en otro usuario
            if (userRepository.existsByUserDNI(userRequest.getUserDNI().trim())) {
                if (!userRequest.getUserDNI().trim().equals(existingUser.getUserDNI())) {
                    throw new RuntimeException("Ya existe un usuario con el DNI " + userRequest.getUserDNI());
                }
            }
            existingUser.setUserDNI(userRequest.getUserDNI().trim());
        }
        if (userRequest.getUserPhone() != null && !userRequest.getUserPhone().trim().isEmpty()) {
            // Validar que el teléfono no exista en otro usuario
            if (userRepository.existsByUserPhone(userRequest.getUserPhone().trim())) {
                if (!userRequest.getUserPhone().trim().equals(existingUser.getUserPhone())) {
                    throw new RuntimeException("Ya existe un usuario con el teléfono " + userRequest.getUserPhone());
                }
            }
            existingUser.setUserPhone(userRequest.getUserPhone().trim());
        }
        if (userRequest.getImageData() != null && !userRequest.getImageData().isEmpty()) {
            existingUser.setImageData(userRequest.getImageData());
        }

        // Solo el ADMIN puede cambiar el estado `active`

        existingUser.setActive(userRequest.getActive());


        // Actualizar el rol si es diferente
        if (userRequest.getRole() != null && !userRequest.getRole().equals(existingUser.getRole())) {
            existingUser.setRole(userRequest.getRole());
        }

        return userRepository.save(existingUser);
    }


    @Override
    public void deleteUser(Long user_id) {  userRepository.deleteById( user_id);

    }

    @Override
    public void deactivateNumber(Long user_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new EntityNotFoundException("Número no encontrado"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public boolean existsUserByUserId(Long userId) {
        return userRepository.existsById(userId);
    }

    // Verificar el estado de la suscripción o prueba gratuita
//    public boolean hasActiveSubscription(Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
//        return user.isHasActiveSubscription() || LocalDate.now().isBefore(user.getTrialExpiryDate());
//    }

    /*
    @Override
    public Mono<EventAndUserDTO> getUserDetailsWithEventCount(Long user_id) {
// Obtener detalles del usuario
        Mono<UserDTO> userMono = webClientBuilder.build()
                .get()
                .uri(apiGatewayBaseUrl + "/api/user_services/users/{userId}", user_id)
                .retrieve()
                .bodyToMono(UserDTO.class);

        // Obtener la cuenta de eventos organizados por el usuario
        Mono<Integer> eventCountMono = webClientBuilder.build()
                .get()
                .uri(apiGatewayBaseUrl + "/api/event_services/events/count/byOrganizer/{organizerId}", user_id)
                .retrieve()
                .bodyToMono(Integer.class);  // Asume que hay un endpoint que retorna el conteo directamente

        // Combinar los resultados
        return Mono.zip(userMono, eventCountMono, UserEventCountDTO::new);

    }*/

    // URL: //localhost:8080/api/event_services/events/byOrganizer/4
    // Method: Lista eventos de Organizador con el ID



}
