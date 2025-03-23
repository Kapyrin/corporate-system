package com.example.userservice.controller;

import com.example.userservice.dto.UserCreateDTO;
import com.example.userservice.dto.UserDetailDTO;
import com.example.userservice.dto.UserSummaryDTO;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UserSummaryDTO> users() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserDetailDTO getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public UserDetailDTO createUser(@RequestBody UserCreateDTO userCreateDTO) {
        return userService.createUser(userCreateDTO);
    }

    @PutMapping("/users/{id}")
    public UserDetailDTO updateUser(@PathVariable("id") Long id, @RequestBody UserCreateDTO userCreateDTO) {
        return userService.updateUser(id, userCreateDTO);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "User with id " + id + " was deleted";
    }
}
