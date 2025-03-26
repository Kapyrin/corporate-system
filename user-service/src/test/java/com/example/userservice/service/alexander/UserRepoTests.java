package com.example.userservice.service.alexander;


import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class UserRepoTests {
    @Autowired
    private UserRepository userRepository;
    private final String NAME = "Alexander";
    private final String EMAIL = "alex@gmail.com";

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User(null, NAME, EMAIL, LocalDateTime.now());
        savedUser = userRepository.save(user);
    }

    @Test
    @DisplayName("Should not create user twice")
    void addUserTwice() {
        userRepository.save(savedUser);
        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
    }

    @Test
    @DisplayName("Should check user name after creation")
    void findUserNameById() {
        assertTrue(userRepository.findById(savedUser.getId()).isPresent());
        User currentUser = userRepository.findById(savedUser.getId()).get();
        assertEquals(NAME, currentUser.getName());
    }

    @Test
    @DisplayName("Should check email after finding")
    void findUserByEmail() {
        assertTrue(userRepository.findByEmail(EMAIL).isPresent());
        assertEquals(EMAIL, userRepository.findByEmail(EMAIL).get().getEmail());
    }

    @Test
    @DisplayName("Should check that user deleted")
    void deleteUserByID() {
        Long id = savedUser.getId();
        assertTrue(userRepository.findById(id).isPresent());
        userRepository.deleteById(id);
        assertFalse(userRepository.findById(id).isPresent());
    }

    @Test
    @DisplayName("Should not change user name")
    void changeUser() {
        Long id = savedUser.getId();
        String nameBeforeSave = savedUser.getName();
        assertTrue(userRepository.findById(id).isPresent());
        savedUser.setEmail("alex@yandex.ru");
        userRepository.save(savedUser);
        assertEquals(nameBeforeSave, savedUser.getName());
    }


}
