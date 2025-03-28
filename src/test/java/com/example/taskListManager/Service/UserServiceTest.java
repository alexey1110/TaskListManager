package com.example.taskListManager.Service;

import com.example.taskListManager.Model.Role;
import com.example.taskListManager.Model.User;
import com.example.taskListManager.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
    void createUser_Success() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = userService.createUser("test@example.com", Role.USER, "password");

        assertNotNull(result);
        assertEquals("test@example.com", result.getUserEmail());
        assertEquals(Role.USER, result.getUserRole());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        var result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("test@example.com", result.getUserEmail());

        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByEmail_Success() {
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(Optional.of(user));

        var result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getUserEmail());

        verify(userRepository).findByUserEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_Success() {
        when(userRepository.findByUserEmail("test@example.com")).thenReturn(Optional.of(user));

        var result = userService.loadUserByUsername("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getUsername());

        verify(userRepository).findByUserEmail("test@example.com");
    }
}