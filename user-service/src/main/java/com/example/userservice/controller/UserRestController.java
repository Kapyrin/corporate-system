package com.example.userservice.controller;

import com.example.userservice.dto.UserCreateDTO;
import com.example.userservice.dto.UserDetailDTO;
import com.example.userservice.dto.UserSummaryDTO;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    private final UserService userService;

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "List of users")
    @GetMapping
    public ResponseEntity<List<UserSummaryDTO>> users() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by id")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDTO> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Create user")
    @ApiResponse(responseCode = "201", description = "User created")
    @ApiResponse(responseCode = "409", description = "Email already exists")
    @PostMapping
    public ResponseEntity<UserDetailDTO> createUser(@RequestBody @Valid UserCreateDTO userCreateDTO) {
        UserDetailDTO createdUser = userService.createUser(userCreateDTO);
        return ResponseEntity.status(201).body(createdUser);
    }

    @Operation(summary = "Update user")
    @ApiResponse(responseCode = "200", description = "User updated")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailDTO> updateUser(@PathVariable("id") Long id,
                                                    @RequestBody @Valid UserCreateDTO userCreateDTO) {
        UserDetailDTO updated = userService.updateUser(id, userCreateDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "204", description = "User deleted")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get user by email")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/by-email")
    public ResponseEntity<UserDetailDTO> getUserByEmail(
            @Parameter(description = "User email")
            @RequestParam("email") String email) {
        return ResponseEntity.ok(
                userService.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email))
        );
    }

    @Operation(summary = "Check if user exists by ID")
    @ApiResponse(responseCode = "200", description = "Returns true if user exists")
    @ApiResponse(responseCode = "404", description = "User not found (optional)")
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> userExists(@PathVariable("id") Long id) {
        boolean exists = userService.existsById(id);
        return ResponseEntity.ok(exists);
    }

}
