package com.example.userservice.service;

import com.example.userservice.dto.UserCreateDTO;
import com.example.userservice.dto.UserDetailDTO;
import com.example.userservice.dto.UserSummaryDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDetailDTO createUser(UserCreateDTO userDto);

    UserDetailDTO getUserById(Long id);

    UserDetailDTO updateUser(Long id, UserCreateDTO userDto);

    void deleteUser(Long id);

    List<UserSummaryDTO> getAllUsers();
    Optional<UserDetailDTO> getUserByEmail(String email);
    boolean existsById(Long id);
}
