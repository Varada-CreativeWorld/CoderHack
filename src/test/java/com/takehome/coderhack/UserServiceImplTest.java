package com.takehome.coderhack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.takehome.coderhack.dtos.UserDTO;
import com.takehome.coderhack.entities.Badge;
import com.takehome.coderhack.entities.User;
import com.takehome.coderhack.exceptions.UserAlreadyExistsException;
import com.takehome.coderhack.exceptions.UserNotFoundException;
import com.takehome.coderhack.repositories.UserRepository;
import com.takehome.coderhack.services.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId("user123");
        user.setUsername("John Doe");
        user.setScore(0);
        user.setBadges(EnumSet.noneOf(Badge.class));

        userDTO = new UserDTO();
        userDTO.setUserId("user123");
        userDTO.setUsername("John Doe");
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAllByOrderByScoreDesc()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(users.size(), result.size());
        verify(userRepository, times(1)).findAllByOrderByScoreDesc();
    }

    @Test
    void testGetUserById_UserExists() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById("user123");

        assertTrue(result.isPresent());
        assertEquals(user.getUserId(), result.get().getUserId());
        verify(userRepository, times(1)).findById("user123");
    }

    @Test
    void testGetUserById_UserNotExists() {
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById("user123");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById("user123");
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.existsById("user123")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registerUser(userDTO);

        assertEquals(user.getUserId(), result.getUserId());
        assertEquals(0, result.getScore());
        assertEquals(EnumSet.noneOf(Badge.class), result.getBadges());
        verify(userRepository, times(1)).existsById("user123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        when(userRepository.existsById("user123")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userDTO));
        verify(userRepository, times(1)).existsById("user123");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserScore_Success() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUserScore("user123", 45);

        assertEquals(45, result.getScore());
        assertTrue(result.getBadges().contains(Badge.CODE_CHAMP));
        verify(userRepository, times(1)).findById("user123");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserScore_UserNotFound() {
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUserScore("user123", 45));
        verify(userRepository, times(1)).findById("user123");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById("user123");

        userService.deleteUser("user123");

        verify(userRepository, times(1)).deleteById("user123");
    }
}

