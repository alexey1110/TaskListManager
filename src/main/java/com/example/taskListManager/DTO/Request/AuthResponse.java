package com.example.taskListManager.DTO.Request;

import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
}
