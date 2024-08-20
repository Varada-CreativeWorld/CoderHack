package com.takehome.coderhack.entities;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    @NotEmpty(message = "User ID must not be empty")
    private String userId;

    @NotEmpty(message = "Username must not be empty")
    private String username;

    private int score = 0;
    private Set<Badge> badges = new HashSet<>();

    // Custom constructor to initialize the user with ID and username
    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
        this.score = 0;
        this.badges = new HashSet<>();
    }
}

