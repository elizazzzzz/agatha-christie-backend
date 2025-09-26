//已实现用户相关API（CRUD、检索等），现有控制器可用于用户注册/查询/更新/删除

package com.agatha.agatha.controller;

import com.agatha.agatha.dto.UserDto;
import com.agatha.agatha.entity.User;
import com.agatha.agatha.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.saveUser(user);
        return new ResponseEntity<>(convertToDto(createdUser), HttpStatus.CREATED);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Optional<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id).map(this::convertToDto);
    }

    @GetMapping("/search")
    public List<UserDto> searchUsers(@RequestParam String query) {
        return userService.searchUsers(query).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        userDetails.setId(id);
        User updatedUser = userService.saveUser(userDetails);
        return convertToDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}