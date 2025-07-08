package com.edu.pe.pagaPeBackend.user.repository;


import com.edu.pe.pagaPeBackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserEmailAndUserPassword(String user_email, String user_password);
    boolean existsById(Long user_id);
    boolean existsByUserEmail(String user_email);
    boolean existsByUserDNI(String userDNI);
    boolean existsByUserPhone(String userPhone);

    Optional<User> findByUserEmail(String email);
    Optional<User> findByUserFirstNameAndUserLastNameAndUserDNI(String userFirstName, String userLastName, String userDNI);

    List<User>findAll();

    List<User> findByActive(boolean active);


}