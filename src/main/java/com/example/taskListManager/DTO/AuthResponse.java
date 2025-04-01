package com.example.taskListManager.DTO;

import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
}
