package com.example.taskListManager.Service;

import com.example.taskListManager.DTO.UserDTO;
import com.example.taskListManager.Exceptions.UserNotFoundException;
import com.example.taskListManager.Model.Role;
import com.example.taskListManager.Model.User;
import com.example.taskListManager.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private UserDTO convertToDTO(User user){
        return new UserDTO(user.getUserId(), user.getUserEmail(), user.getUserRole());
    }

    @Transactional
    public UserDTO createUser (String email, Role role, String password){
        User user = new User();

        String hashedPassword = passwordEncoder.encode(password);
        user.setUserPassword(hashedPassword);
        user.setUserEmail(email);
        user.setUserRole(role);

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public List<UserDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById (Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserDTO getUserByEmail (String email){
        return userRepository.findByUserEmail(email)
                .map(this::convertToDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserDTO updateUserEmail(Long id, String email, Role role, String password){
        return userRepository.findById(id)
                .map(user -> {
                    user.setUserEmail(email);
                    user.setUserRole(role);
                    user.setUserPassword(passwordEncoder.encode(password));
                    return convertToDTO(user);
                })
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public void deleteUser (Long id){
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email: " + username));
    }
}
