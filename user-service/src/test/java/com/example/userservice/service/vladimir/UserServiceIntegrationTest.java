package com.example.userservice.service.vladimir;

import com.example.userservice.dto.UserCreateDTO;
import com.example.userservice.dto.UserDetailDTO;
import com.example.userservice.dto.UserSummaryDTO;
import com.example.userservice.entity.User;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    private static final Long NON_EXISTENT_ID = 999L;
    private static final String NON_EXISTING_EMAIL = "nonexistent@mail.com";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user1 = userRepository.save(new User(null, "Existing1", "exist1@mail.com", LocalDateTime.now()));
        user2 = userRepository.save(new User(null, "Existing2", "exist2@mail.com", LocalDateTime.now()));
    }

    @Test
    void createUser_success() {
        UserCreateDTO dto = new UserCreateDTO("NewUser", "new@mail.com");

        UserDetailDTO result = userService.createUser(dto);

        assertNotNull(result.getId());
        assertEquals("NewUser", result.getName());
        assertEquals("new@mail.com", result.getEmail());
    }

    @Test
    void createUser_duplicateEmail_throwsException() {
        UserCreateDTO dto = new UserCreateDTO("Duplicate", "exist1@mail.com");

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(dto));
    }

    @Test
    void getUserById_success() {
        UserDetailDTO result = userService.getUserById(user1.getId());

        assertEquals(user1.getId(), result.getId());
        assertEquals(user1.getName(), result.getName());
        assertEquals(user1.getEmail(), result.getEmail());

        UserDetailDTO result2 = userService.getUserById(user2.getId());

        assertEquals(user2.getId(), result2.getId());
        assertEquals(user2.getName(), result2.getName());
        assertEquals(user2.getEmail(), result2.getEmail());
    }

    @Test
    void getUserById_notFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(NON_EXISTENT_ID));
    }

    @Test
    void updateUser_success() {
        UserCreateDTO dto = new UserCreateDTO("UpdatedName", "updated@mail.com");

        UserDetailDTO updated = userService.updateUser(user1.getId(), dto);

        assertEquals("UpdatedName", updated.getName());
        assertEquals("updated@mail.com", updated.getEmail());
    }

    @Test
    void updateUser_notFound() {
        UserCreateDTO dto = new UserCreateDTO("NoUser", "nouser@mail.com");

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(NON_EXISTENT_ID, dto));
    }

    @Test
    void deleteUser_success() {
        assertTrue(userRepository.existsById(user1.getId()));

        userService.deleteUser(user1.getId());

        assertFalse(userRepository.existsById(user1.getId()));
    }

    @Test
    void deleteUser_notFound() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(NON_EXISTENT_ID));
    }

    @Test
    void getAllUsers_success() {
        List<UserSummaryDTO> allUsers = userService.getAllUsers();

        assertEquals(2, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(u -> u.getEmail().equals("exist1@mail.com")));
        assertTrue(allUsers.stream().anyMatch(u -> u.getEmail().equals("exist2@mail.com")));
    }

    @Test
    void getAllUsers_empty() {
        userRepository.deleteAll();

        List<UserSummaryDTO> users = userService.getAllUsers();

        assertTrue(users.isEmpty());
    }

    @Test
    void getUserByEmail_shouldReturnUserDetailDTO() {
        Optional<UserDetailDTO> result = userService.getUserByEmail(user1.getEmail());

        assertTrue(result.isPresent());

        UserDetailDTO dto = result.get();
        assertEquals(user1.getName(), dto.getName());
        assertEquals(user1.getEmail(), dto.getEmail());
        assertNotNull(dto.getCreatedAt());
    }

    @Test
    void getUserByEmail_shouldReturnEmptyOptional_ifUserNotFound() {
       Optional<UserDetailDTO> result = userService.getUserByEmail(NON_EXISTING_EMAIL);

        assertTrue(result.isEmpty());
    }

}
