package com.edu.pe.pagaPeBackend.user.service;



import com.edu.pe.pagaPeBackend.user.model.User;

import java.util.List;

public interface UserService {
    public abstract User createUser(User user);
    public abstract User getUserById(Long user_id);
    public abstract User getUserByEmail(String email);

    public abstract User getUserByNameAndDNI(String firstName,String lastName, String dni);
    public abstract boolean checkDNIExists(String dni);
    public abstract User updateUser(User user);

    public abstract User updateUser2 ( User existingUser, User userRequest);
    public abstract void deleteUser(Long user_id);

    public void deactivateNumber(Long user_id);
    public abstract boolean existsUserByUserId(Long user_id);

    public abstract List<User> getAllUsers();

    public List<User> getAllUsersByStatus(boolean status);


}
