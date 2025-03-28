package com.example.taskListManager.Service;

import com.example.taskListManager.DTO.UserDTO;
import com.example.taskListManager.Exceptions.UserNotFoundException;
import com.example.taskListManager.Model.Role;
import com.example.taskListManager.Model.User;
import com.example.taskListManager.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                        UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userService = userService;
    }
    @Transactional
    public UserDTO register (String email, String password){
        if(userRepository.findByUserEmail(email).isPresent()){
            throw new UserNotFoundException("User with email not found");
        }
        return userService.createUser(email, Role.USER, password);
    }

    public String authenticate(String email, String password){
        Optional<User> userOpt = userRepository.findByUserEmail(email);
        if (userOpt.isEmpty()){
            throw new IndexOutOfBoundsException("Incorrect email or password");
        }
        User user = userOpt.get();

        if(!passwordEncoder.matches(password, user.getUserPassword())){
            throw new IndexOutOfBoundsException("Incorrect email or password");
        }
        return jwtService.generateToken(user);
    }
}
