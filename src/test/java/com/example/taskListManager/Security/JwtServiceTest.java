package com.example.taskListManager.Security;

import com.example.taskListManager.Model.User;
import com.example.taskListManager.Service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;
    private String secretKey = "testSecretKeyForJwtTestingJwtTestingJwtTestingJwtTestingJwtTestingJwtTestingJwtTesting";
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
        jwtService.secretKey = secretKey;

        user = new User();
        user.setUserEmail("test@example.com");
        user.setUserPassword("password");
        user.setUserId(null);
    }

    @Test
    void generateToken_Success() {
        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_Success() {
        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);

        assertNotNull(username);
        assertEquals("test@example.com", username);
    }

    @Test
    void validateToken_Success() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@example.com", "password", true, true, true, true, Collections.emptyList()
        );
        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }
}
