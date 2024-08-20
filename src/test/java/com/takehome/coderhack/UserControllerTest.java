package com.takehome.coderhack;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takehome.coderhack.controllers.UserController;
import com.takehome.coderhack.dtos.UserDTO;
import com.takehome.coderhack.entities.User;
import com.takehome.coderhack.exceptions.GlobalExceptionHandler;
import com.takehome.coderhack.exceptions.UserAlreadyExistsException;
import com.takehome.coderhack.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();
    

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())  // Set the GlobalExceptionHandler
                .build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user = new User();
        user.setUserId("user123");
        user.setUsername("John Doe");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user123"))
                .andExpect(jsonPath("$[0].username").value("John Doe"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_UserExists() throws Exception {
        User user = new User();
        user.setUserId("user123");
        user.setUsername("John Doe");

        when(userService.getUserById("user123")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/{userId}", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.username").value("John Doe"));

        verify(userService, times(1)).getUserById("user123");
    }


    @Test
    void testRegisterUser_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId("user123");
        userDTO.setUsername("John Doe");

        User user = new User();
        user.setUserId("user123");
        user.setUsername("John Doe");

        when(userService.registerUser(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.username").value("John Doe"));

        verify(userService, times(1)).registerUser(any(UserDTO.class));
    }

    @Test
    void testUpdateUserScore_Success() throws Exception {
        User user = new User();
        user.setUserId("user123");
        user.setUsername("John Doe");
        user.setScore(45);

        when(userService.updateUserScore("user123", 45)).thenReturn(user);

        mockMvc.perform(put("/users/{userId}", "user123")
                .param("score", "45"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.score").value(45));

        verify(userService, times(1)).updateUserScore("user123", 45);
    }


    @Test
    void testDeleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser("user123");

        mockMvc.perform(delete("/users/{userId}", "user123"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser("user123");
    }

    @Test
    void testGetUserById_UserNotFound() throws Exception {
        // Arrange: Simulate userService.getUserById() returning an empty Optional
        when(userService.getUserById(anyString())).thenReturn(Optional.empty());

        // Act & Assert: Perform the GET request and expect a 404 status
        mockMvc.perform(get("/users/nonExistingUserId"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Provided userId does not exist."));
    }

    @Test
    void testRegisterUser_MissingUsernameOrUserId() throws Exception {
        // Arrange: Create UserDTO with missing fields
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId("");  // Missing userId
        userDTO.setUsername("");  // Missing username

        // Act & Assert: Perform the POST request and expect a 400 status with validation error messages
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userId").value("User ID must not be empty"))
                .andExpect(jsonPath("$.username").value("Username must not be empty"));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() throws Exception {
        // Arrange: Simulate userService.registerUser() throwing a UserAlreadyExistsException
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId("123");
        userDTO.setUsername("testuser");

        when(userService.registerUser(any(UserDTO.class))).thenThrow(new UserAlreadyExistsException("User already exists with userId: 123"));

        // Act & Assert: Perform the POST request and expect a 400 status with the appropriate error message
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("User already exists with userId: 123"));
    }

    
}
