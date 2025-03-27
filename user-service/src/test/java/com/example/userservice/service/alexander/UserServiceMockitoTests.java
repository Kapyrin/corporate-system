package com.example.userservice.service.alexander;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceMockitoTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImplementation userService;

    @Test
    void createUser_newUserSaved() {
        UserCreateDTO dto = new UserCreateDTO("alex", "alex@gmail.ru");
        User newUser = new User(null, "alex", "alex@gmail.ru", LocalDateTime.now());
        User savedUser = new User(1L, "alex", "alex@gmail.ru", LocalDateTime.now());
        UserDetailDTO detailDTO = new UserDetailDTO(1L, "alex", "alex@gmail.ru", LocalDateTime.now());

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userMapper.toDetailDto(savedUser)).thenReturn(detailDTO);

        userService.createUser(dto);

        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userMapper, times(1)).toEntity(dto);
        verify(userRepository, times(1)).save(newUser);
        verify(userMapper, times(1)).toDetailDto(savedUser);
    }

    @Test
    void createUser_newUserNotSaved_throwsException() {
        UserCreateDTO dto = new UserCreateDTO("alex", "alex@gmail.ru");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        userService.createUser(dto);

        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_returnsUser() {
        Long id = 1L;
        User user = new User(id, "alex", "alex@gmail.ru", LocalDateTime.now());
        UserDetailDTO detailDTO = new UserDetailDTO(id, "alex", "alex@gmail.ru", LocalDateTime.now());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDetailDto(user)).thenReturn(detailDTO);

        userService.getUserById(id);

        verify(userRepository, times(1)).findById(id);
        verify(userMapper, times(1)).toDetailDto(user);
    }

    @Test
    void getUserById_noSuchUser_throwsException() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));

        verify(userRepository, times(1)).findById(id);
        verify(userMapper, never()).toDetailDto(any());
    }

    @Test
    void updateUser_userUpdated_success() {
        Long id = 1L;
        UserCreateDTO updateDTO = new UserCreateDTO("alex", "alex@gmail.ru");
        User user = new User(id, "alex", "alex@gmail.ru", LocalDateTime.now());
        UserDetailDTO detailDTO = new UserDetailDTO(id, "alex", "alex@gmail.ru", LocalDateTime.now());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(updateDTO.getEmail())).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDetailDto(user)).thenReturn(detailDTO);

        userService.updateUser(id, updateDTO);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).existsByEmail(updateDTO.getEmail());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDetailDto(user);

    }

    @Test
    void updateUser_tryingUseExistingEmail_throwsException() {
        Long id = 1L;
        UserCreateDTO updateDTO = new UserCreateDTO("alex", "alex@gmail.ru");
        User user = new User(id, "alex", "notAlex@gmail.ru", LocalDateTime.now());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(updateDTO.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(id, updateDTO));

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).existsByEmail(updateDTO.getEmail());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toDetailDto(any());

    }

    @Test
    void updateUser_UserNotExists_throwException() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(id, new UserCreateDTO()));

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toDetailDto(any());
    }

    @Test
    void deleteUser_success() {
        Long id = 1L;

        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUser(id);

        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteUser_noSuchUser_throwsException() {
        Long id = 1L;

        when(userRepository.existsById(id)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(id));

        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void getAllUsers_returnUsersSummaryList() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "alex", "alex@gmail.ru", LocalDateTime.now()));
        users.add(new User(2L, "Vladimir", "vlad@gmail.ru", LocalDateTime.now()));
        List<UserSummaryDTO> userSummaryDTOS = new ArrayList<>();
        userSummaryDTOS.add(new UserSummaryDTO(1L, "alex", "alex@gmail.ru"));
        userSummaryDTOS.add(new UserSummaryDTO(2L, "Vladimir", "vlad@gmail.ru"));

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toSummaryDtoList(users)).thenReturn(userSummaryDTOS);

        userService.getAllUsers();

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toSummaryDtoList(users);
    }

    @Test
    void getUserByEmail_returnsUser_success() {
        Long id = 1L;
        String email = "alex@gmail.ru";
        User user = new User(id, "alex", email, LocalDateTime.now());
        UserDetailDTO userDetailDTO = new UserDetailDTO(id, "alex", email, LocalDateTime.now());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toDetailDto(user)).thenReturn(userDetailDTO);

        userService.getUserByEmail(email);

        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, times(1)).toDetailDto(user);
    }

    @Test
    void getUserByEmail_noSuchUser_returnNullUserDetailDTO() {
        String email = "alex@gmail.ru";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        userService.getUserByEmail(email);

        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, never()).toDetailDto(any());
    }


}
