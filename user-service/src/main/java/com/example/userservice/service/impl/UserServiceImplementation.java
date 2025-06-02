package com.example.userservice.service.impl;

import com.example.userservice.dto.UserCreateDTO;
import com.example.userservice.dto.UserDetailDTO;
import com.example.userservice.dto.UserSummaryDTO;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.exception.RoleNotFoundException;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDetailDTO createUser(UserCreateDTO userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException(userDto.getEmail());
        }

        User user = userMapper.toEntity(userDto);

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RoleNotFoundException("USER"));
        user.setRole(role);

        return userMapper.toDetailDto(userRepository.save(user));
    }


    @Override
    public UserDetailDTO getUserById(Long id) {
        return userMapper.toDetailDto(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    public UserDetailDTO updateUser(Long id, UserCreateDTO userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userRepository.existsByEmail(userDto.getEmail()) &&
                !existingUser.getEmail().equals(userDto.getEmail())) {
            throw new UserAlreadyExistsException(userDto.getEmail());
        }

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDetailDto(updatedUser);
    }


    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserSummaryDTO> getAllUsers() {
        return userMapper.toSummaryDtoList(userRepository.findAll());
    }

    @Override
    public Optional<UserDetailDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDetailDto);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

}
