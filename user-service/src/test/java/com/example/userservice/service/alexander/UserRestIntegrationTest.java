package com.example.userservice.service.alexander;

import com.example.userservice.dto.UserCreateDTO;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User savedUser;
    private static final String NAME = "alex";
    private static final String EMAIL = "alex@gmail.com";
    private static final String EMPTY_EMAIL = "nullUser@gmail.com";
    private static final Long EMPTY_ID = 0L;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setName(NAME);
        user.setEmail(EMAIL);
        savedUser = userRepository.save(user);
    }

    @Test
    @DisplayName("GET /api/users list of users with one user")
    void getAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andExpect(jsonPath("$[0].email").value(EMAIL));
    }

    @Test
    @DisplayName("Get /api/users/id existing user by ID")
    void getExistingUser() throws Exception {
        String url = "/api/users/" + savedUser.getId();
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    @DisplayName("Get /api/users/id not existing user by ID")
    void getNotExistingUser() throws Exception {
        String url = "/api/users/" + EMPTY_ID;
        mockMvc.perform(get(url))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error").value("User with id " + EMPTY_ID + " was not found"));
    }

    @Test
    @DisplayName("Post /api/users create user")
    void createUser() throws Exception {
        UserCreateDTO dto = new UserCreateDTO("Vladimir", "vlad@gmail.com");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name").value("Vladimir"))
                .andExpect(jsonPath("$.email").value("vlad@gmail.com"));
    }

    @Test
    @DisplayName("Post /api/users existing user don`t create user")
    void createUserWhenEmailExists() throws Exception {
        UserCreateDTO dto = new UserCreateDTO("alex", "alex@gmail.com");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is(409));
    }

    @Test
    @DisplayName("Put /api/users/id existing user update")
    void updateUserWhenExists() throws Exception {
        UserCreateDTO dto = new UserCreateDTO("Alexander", "alex@gmail.com");
        String url = "/api/users/" + savedUser.getId();
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alexander"))
                .andExpect(jsonPath("$.email").value("alex@gmail.com"));
    }

    @Test
    @DisplayName("Put /api/users/id not existing user update")
    void updateUserWhenNotExists() throws Exception {
        UserCreateDTO dto = new UserCreateDTO("Alexander", "alex@gmail.com");
        String url = "/api/users/" + EMPTY_ID;
        mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error").value("User with id " + EMPTY_ID + " was not found"));
    }

    @Test
    @DisplayName("Delete /api/users existing user delete")
    void deleteExistingUser() throws Exception {
        String url = "/api/users/" + savedUser.getId();
        mockMvc.perform(delete(url))
                .andExpect(status().is(204));
    }

    @Test
    @DisplayName("Delete /api/users not existing user delete")
    void deleteNotExistingUser() throws Exception {
        String url = "/api/users/" + EMPTY_ID;
        mockMvc.perform(delete(url))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error").value("User with id " + EMPTY_ID + " was not found"));
    }

    @Test
    @DisplayName("Get /api/users/by-email existing user by email")
    void getExistingUserByEmail() throws Exception {
        mockMvc.perform(get("/api/users/by-email").param("email", EMAIL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.email").value(EMAIL));
    }

    @Test
    @DisplayName("Get /api/users/by-email not existing user by email")
    void getNotExistingUserByEmail() throws Exception {
        mockMvc.perform(get("/api/users/by-email").param("email", EMPTY_EMAIL))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error").value("User with email " + EMPTY_EMAIL + " was not found"));
    }

}
