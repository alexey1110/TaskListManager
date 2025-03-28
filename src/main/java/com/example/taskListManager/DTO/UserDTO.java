package com.example.taskListManager.DTO;

import com.example.taskListManager.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String usrEmail;
    private Role userRole;
}
