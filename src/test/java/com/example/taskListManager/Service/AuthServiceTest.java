package com.example.taskListManager.Service;

import com.example.taskListManager.DTO.UserDTO;
import com.example.taskListManager.Model.Role;
import com.example.taskListManager.Model.User;
import com.example.taskListManager.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUserEmail("test@example.com");
        user.setUserRole(Role.USER);
        user.setUserPassword("hashedPassword");
    }

    @Test
    void register_Success() {
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(Optional.empty());
        when(userService.createUser("test@example.com", Role.USER, "password"))
                .thenReturn(new UserDTO(1L, "test@example.com", Role.USER));

        var result = authService.register("test@example.com", "password");

        assertNotNull(result);
        assertEquals("test@example.com", result.getUserEmail());
        assertEquals(Role.USER, result.getUserRole());

        verify(userRepository).findByUserEmail("test@example.com");
        verify(userService).createUser("test@example.com", Role.USER, "password");
    }

    @Test
    void authenticate_Success() {
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt_token");

        var result = authService.authenticate("test@example.com", "password");

        assertNotNull(result);
        assertEquals("jwt_token", result);

        verify(userRepository).findByUserEmail("test@example.com");
        verify(passwordEncoder).matches("password", "hashedPassword");
        verify(jwtService).generateToken(user);
    }
}
