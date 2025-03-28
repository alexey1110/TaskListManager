package com.example.taskListManager.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "USER", schema = "public")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "USER_EMAIL",nullable = false, unique = true)
    private String userEmail;

    @Column(name = "USER_PASSWORD",nullable = false)
    private String userPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_ROLE", nullable = false)
    private Role userRole;

    @OneToMany(mappedBy = "author")
    private List<Task> createdTasks;

    @OneToMany(mappedBy = "executor")
    private List<Task> assignedTasks;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
