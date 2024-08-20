package com.takehome.coderhack.services;

import java.util.List;
import java.util.Optional;

import com.takehome.coderhack.dtos.UserDTO;
import com.takehome.coderhack.entities.User;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(String userId);
    User registerUser(UserDTO user);
    User updateUserScore(String userId, int score);
    void deleteUser(String userId);
}

