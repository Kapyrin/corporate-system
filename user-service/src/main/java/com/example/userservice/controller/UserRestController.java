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
    public UserDetailDTO createUser(@RequestBody User user) {
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setName(user.getName());
        createDTO.setEmail(user.getEmail());
        return userService.createUser(createDTO);
    }

    @PutMapping("/users")
    public UserDetailDTO updateUser(@RequestBody User user) {
        UserCreateDTO createDTO = new UserCreateDTO();
        createDTO.setName(user.getName());
        createDTO.setEmail(user.getEmail());
        return userService.updateUser(user.getId(), createDTO);
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "User with id " + id + " was deleted";
    }
}
