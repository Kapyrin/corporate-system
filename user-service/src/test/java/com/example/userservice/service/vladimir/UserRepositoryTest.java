package com.example.userservice.service.vladimir;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User(null, "Vladimir", "vlad@mail.com", LocalDateTime.now());
        savedUser = userRepository.save(user);
    }

    @Test
    @DisplayName("Should find user by ID")
    void findByIdUser() {
        Optional<User> result = userRepository.findById(savedUser.getId());

        assertTrue(result.isPresent());
        assertEquals("Vladimir", result.get().getName());
        assertEquals("vlad@mail.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Should return true when email exists")
    void existsByEmailUser() {
        boolean exists = userRepository.existsByEmail("vlad@mail.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void existsByEmailUserNotFound() {
        boolean exists = userRepository.existsByEmail("unknown@mail.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Should update user name")
    void updateUserName() {
        savedUser.setName("Updated Name");
        User updatedUser = userRepository.save(savedUser);

        assertEquals("Updated Name", updatedUser.getName());
    }

    @Test
    @DisplayName("Should delete user by ID")
    void deleteUserById() {
        Long userId = savedUser.getId();

        assertTrue(userRepository.existsById(userId));

        userRepository.deleteById(userId);

        assertFalse(userRepository.existsById(userId));
    }

    @Test
    @DisplayName("Should find all users")
    void findAllUsers() {
        List<User> users = userRepository.findAll();

        assertEquals(1, users.size());
        assertEquals("Vladimir", users.get(0).getName());
        assertEquals("vlad@mail.com", users.get(0).getEmail());
    }
}
