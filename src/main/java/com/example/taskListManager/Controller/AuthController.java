package com.example.taskListManager.Controller;

import com.example.taskListManager.DTO.Request.AuthRequest;
import com.example.taskListManager.DTO.Request.AuthResponse;
import com.example.taskListManager.DTO.UserDTO;
import com.example.taskListManager.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

@Tag(name = "Аутентификация", description = "Методы для регистрации и входа пользователей")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Регистрация нового пользователя", description = "Создает новую учетную запись пользователя с указанным email и паролем.")
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid AuthRequest authRequest){
        logger.info("Регистрация нового пользователя: {}" + authRequest.getEmail());
        return ResponseEntity.ok(authService.register(authRequest.getEmail(), authRequest.getPassword()));
    }

    @Operation(summary = "Авторизация пользователя", description = "Авторизует пользователя и возвращает JWT-токен.")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest authRequest){
        logger.info("Авторизация пользователя: {}" + authRequest.getEmail());
        return ResponseEntity.ok(new AuthResponse(authService.authenticate(authRequest.getEmail(), authRequest.getPassword())));
    }
}
