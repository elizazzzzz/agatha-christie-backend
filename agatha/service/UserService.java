package com.agatha.agatha.service;

import com.agatha.agatha.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User saveUser(User user);
    void deleteUser(Long id);
    List<User> searchUsers(String query);
}