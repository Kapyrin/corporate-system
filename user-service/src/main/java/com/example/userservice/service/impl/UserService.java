package com.example.userservice.service.impl;

public interface UserService {

    UserDetailDTO createUser(UserCreateDTO userDto);

    UserDetailDTO getUserById(Long id);

    UserDetailDTO updateUser(Long id, UserCreateDTO userDto);

    void deleteUser(Long id);

    List<UserSummaryDTO> getAllUsers();
}
