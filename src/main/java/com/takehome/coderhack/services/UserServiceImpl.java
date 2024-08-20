package com.takehome.coderhack.services;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takehome.coderhack.dtos.UserDTO;
import com.takehome.coderhack.entities.Badge;
import com.takehome.coderhack.entities.User;
import com.takehome.coderhack.exceptions.UserAlreadyExistsException;
import com.takehome.coderhack.exceptions.UserNotFoundException;
import com.takehome.coderhack.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByScoreDesc();
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        // Check if user with the same ID already exists
        if (userRepository.existsById(userDTO.getUserId())) {
            throw new UserAlreadyExistsException("User with ID " + userDTO.getUserId() + " already exists");
        }

        // Create a new User object from the DTO
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUsername(userDTO.getUsername());
        user.setScore(0); // Initial score
        user.setBadges(EnumSet.noneOf(Badge.class)); // Initial badges

        return userRepository.save(user);
    }

    @Override
    public User updateUserScore(String userId, int score) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " does not exist"));

        user.setScore(score);
        user.setBadges(EnumSet.noneOf(Badge.class));
        
        if (score >= 1 && score < 30) {
            user.getBadges().add(Badge.CODE_NINJA);
        }
        if (score >= 30 && score < 60) {
            user.getBadges().add(Badge.CODE_CHAMP);
        }
        if (score >= 60 && score <= 100) {
            user.getBadges().add(Badge.CODE_MASTER);
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}

