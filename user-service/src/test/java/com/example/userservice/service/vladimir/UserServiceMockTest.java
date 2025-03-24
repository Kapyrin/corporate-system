package com.example.userservice.service.vladimir;

import com.example.userservice.dto.UserCreateDTO;
import com.example.userservice.dto.UserDetailDTO;
import com.example.userservice.dto.UserSummaryDTO;
import com.example.userservice.entity.User;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.impl.UserServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceMockTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImplementation userService;

    @Test
    void createUser_shouldSaveUserAndReturnDetailDto() {
        UserCreateDTO dto = new UserCreateDTO("Vladimir", "vladimir@mail.ru");
        User userEntity = new User(null, "Vladimir", "vladimir@mail.ru", LocalDateTime.now());
        User savedUser = new User(1L, "Vladimir", "vladimir@mail.ru", LocalDateTime.now());
        UserDetailDTO detailDTO = new UserDetailDTO(1L, "Vladimir", "vladimir@mail.ru", LocalDateTime.now());

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUser);
        when(userMapper.toDetailDto(savedUser)).thenReturn(detailDTO);

        UserDetailDTO result = userService.createUser(dto);

        assertEquals(detailDTO, result);

        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toEntity(dto);
        verify(userMapper, times(1)).toDetailDto(savedUser);

    }

    @Test
    void createUser_shouldThrowExceptionIfEmailExists() {
        UserCreateDTO dto = new UserCreateDTO("Vladimir", "vladimir@mail.ru");
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(dto));
        verify(userRepository).existsByEmail(dto.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_shouldReturnUserDetailDTO() {
        Long userId = 1L;
        User user = new User(userId, "Vladimir", "vladimir@mail.ru", LocalDateTime.now());
        UserDetailDTO expectedDto = new UserDetailDTO(userId, "Vladimir", "vladimir@mail.ru", user.getCreatedAt());

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(userMapper.toDetailDto(user)).thenReturn(expectedDto);

        UserDetailDTO result = userService.getUserById(userId);

        assertEquals(expectedDto, result);
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toDetailDto(user);
    }

    @Test
    void getUserById_shouldThrowUserNotFoundException() {
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, never()).toDetailDto(any());
    }

    @Test
    void updateUser_shouldUpdateUserAndReturnDetailDTO() {
        Long userId = 1L;
        UserCreateDTO updateDto = new UserCreateDTO("NewName", "newemail@mail.ru");

        User existingUser = new User(userId, "OldName", "old@mail.ru", LocalDateTime.now());
        User updatedUser = new User(userId, "NewName", "newemail@mail.ru", existingUser.getCreatedAt());
        UserDetailDTO expectedDto = new UserDetailDTO(userId, "NewName", "newemail@mail.ru", existingUser.getCreatedAt());

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toDetailDto(updatedUser)).thenReturn(expectedDto);

        UserDetailDTO result = userService.updateUser(userId, updateDto);

        assertEquals(expectedDto, result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
        verify(userMapper, times(1)).toDetailDto(updatedUser);
    }

    @Test
    void updateUser_shouldThrowExceptionIfEmailAlreadyExists() {
        Long userId = 1L;
        UserCreateDTO updateDto = new UserCreateDTO("Name", "existing@mail.ru");

        User existingUser = new User(userId, "OldName", "old@mail.ru", LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(existingUser));
        when(userRepository.existsByEmail(updateDto.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(userId, updateDto));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).existsByEmail(updateDto.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_shouldDeleteExistingUser() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_shouldThrowUserNotFoundExceptionIfNotExist() {
        Long userId = 99L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void getAllUsers_shouldReturnUserSummaryDtoList() {
        User user1 = new User(1L, "Vladimir", "vlad@mail.ru", LocalDateTime.now());
        User user2 = new User(2L, "Alex", "alex@mail.ru", LocalDateTime.now());

        List<User> users = List.of(user1, user2);

        UserSummaryDTO dto1 = new UserSummaryDTO(1L, "Vladimir", "vlad@mail.ru");
        UserSummaryDTO dto2 = new UserSummaryDTO(2L, "Alex", "alex@mail.ru");

        List<UserSummaryDTO> expectedDtoList = List.of(dto1, dto2);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toSummaryDtoList(users)).thenReturn(expectedDtoList);

        List<UserSummaryDTO> result = userService.getAllUsers();

        assertEquals(expectedDtoList, result);
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toSummaryDtoList(users);
    }

    @Test
    void getAllUsers_shouldReturnEmptyListIfNoUsers() {
        List<User> emptyList = List.of();
        List<UserSummaryDTO> expectedEmptyDtoList = List.of();

        when(userRepository.findAll()).thenReturn(emptyList);
        when(userMapper.toSummaryDtoList(emptyList)).thenReturn(expectedEmptyDtoList);

        List<UserSummaryDTO> result = userService.getAllUsers();

        assertEquals(expectedEmptyDtoList, result);
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toSummaryDtoList(emptyList);
    }

}
