package com.example.userservice.service;

import com.example.userservice.dto.UserCreateDTO;
import com.example.userservice.dto.UserDetailDTO;
import com.example.userservice.dto.UserSummaryDTO;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapperImplementation;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapperImplementation userMapper;

    @Override
    public UserDetailDTO createUser(UserCreateDTO userDto) {
        return userMapper.toDetailDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    @Override
    public UserDetailDTO getUserById(Long id) {
        return userMapper.toDetailDto(userRepository.findById(id).orElse(new User()));
    }

    @Override
    public UserDetailDTO updateUser(Long id, UserCreateDTO userDto) {
        User user = userMapper.toEntity(userDto);
        user.setId(id);
        return userMapper.toDetailDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserSummaryDTO> getAllUsers() {
        return userMapper.toSummaryDtoList(userRepository.findAll());
    }
}
