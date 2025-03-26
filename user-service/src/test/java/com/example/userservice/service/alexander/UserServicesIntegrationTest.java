package com.example.userservice.service.alexander;

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
public class UserServicesIntegrationTest {
    private static final Long EMPTY_ID = 0L;
    private static final String EMPTY_EMAIL = "empty@email.com";
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User firstUser;
    private User secondUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        firstUser = userRepository.save(new User(null, "Alex", "alex@gmail.com", LocalDateTime.now()));
        secondUser = userRepository.save(new User(null, "Vladimir", "vlad@gmail.com", LocalDateTime.now()));
    }

    @Test
    void createUser_duplicateName_success() {
        UserCreateDTO dto = new UserCreateDTO("Alex", "new@mail.com");

        UserDetailDTO result = userService.createUser(dto);

        assertNotNull(result.getId());
        assertEquals("Alex", result.getName());
        assertEquals("new@mail.com", result.getEmail());
    }

    @Test
    void createUser_duplicateEmail_otherName_throwsException() {
        UserCreateDTO dto = new UserCreateDTO("Alexander Roznov", "alex@gmail.com");
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(dto));
    }

    @Test
    void getUserById_success() {
        UserDetailDTO result = userService.getUserById(firstUser.getId());
        assertNotNull(result);
        assertEquals(firstUser.getName(), result.getName());
        assertNotEquals(secondUser.getName(), result.getName());
    }

    @Test
    void getUserById_noUserWithId_throwsException() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(EMPTY_ID));
    }

    @Test
    void updateUser_EmailAlreadyUsed_throwsException() {
        assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(secondUser.getId(), new UserCreateDTO(secondUser.getName(), firstUser.getEmail())));
    }

    @Test
    void updateUser_success() {
        String newFirstUserName = "Pavel";
        assertEquals(newFirstUserName, userService.updateUser(firstUser.getId(), new UserCreateDTO(newFirstUserName, firstUser.getEmail())).getName());
    }

    @Test
    void deleteUser_noUserWithId_throwsException() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(EMPTY_ID));
    }

    @Test
    void deleteAllUsers_success() {
        userService.deleteUser(firstUser.getId());
        userService.deleteUser(secondUser.getId());
        List<UserSummaryDTO> users = userService.getAllUsers();
        assertEquals(0, users.size());
    }

    @Test
    void getTwoUsers_success() {
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test
    void getUserByEmail_success() {
        Optional<UserDetailDTO> result = userService.getUserByEmail(firstUser.getEmail());

        assertTrue(result.isPresent());

        UserDetailDTO dto = result.get();
        assertEquals(firstUser.getName(), dto.getName());
        assertEquals(firstUser.getEmail(), dto.getEmail());
        assertNotNull(dto.getCreatedAt());
    }

    @Test
    void getUserByEmail_userIsNotPresent() {
        Optional<UserDetailDTO> result = userService.getUserByEmail(EMPTY_EMAIL);
        assertFalse(result.isPresent());
    }
}
