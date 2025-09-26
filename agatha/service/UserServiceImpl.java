package com.agatha.agatha.service;

import com.agatha.agatha.entity.User;
import com.agatha.agatha.repository.UserRepository;
import com.agatha.agatha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> searchUsers(String query) {
        // 使用已存在的方法，通过用户名或邮箱进行模糊查询
        return userRepository.findByUsernameContainingOrEmailContaining(query, query);
    }
}

