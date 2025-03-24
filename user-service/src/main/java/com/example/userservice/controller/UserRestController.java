package com.example.userservice.controller;

import com.example.userservice.dto.UserCreateDTO;
import com.example.userservice.dto.UserDetailDTO;
import com.example.userservice.dto.UserSummaryDTO;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {
    //    @Autowired опять autowired
    private final UserService userService;

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of users")
    @GetMapping
    public List<UserSummaryDTO> users() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by id")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public UserDetailDTO getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "201", description = "User created")
    @ApiResponse(responseCode = "409", description = "Email already exists")
    @PostMapping
                public ResponseEntity<UserDetailDTO> createUser(@RequestBody @Valid UserCreateDTO userCreateDTO) {
    UserDetailDTO createdUser = userService.createUser(userCreateDTO);
        return ResponseEntity.status(201).body(createdUser); // вернет 201
    }

    @Operation(summary = "Update user")
    @ApiResponse(responseCode = "200", description = "User updated")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public UserDetailDTO updateUser(@PathVariable("id") Long id, @RequestBody @Valid UserCreateDTO userCreateDTO) {
        return userService.updateUser(id, userCreateDTO);
    }

    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "204", description = "User deleted")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
