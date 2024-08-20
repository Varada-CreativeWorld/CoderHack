package com.takehome.coderhack.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO {

    @NotBlank(message = "User ID must not be empty")
    private String userId;

    @NotBlank(message = "Username must not be empty")
    private String username;
}
