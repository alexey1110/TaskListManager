package com.example.taskListManager.Security;

import com.example.taskListManager.Service.JwtService;
import com.example.taskListManager.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            log.error("JWT Token is missing or invalid format");
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        log.info("Extracted JWT Token: {}" + token);
        String userEmail = jwtService.extractUsername(token);
        if (userEmail == null) {
            System.out.println("Failed to extract username from JWT Token");
            filterChain.doFilter(request, response);
            return;
        }
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(userEmail);
        } catch (Exception e) {
            log.error("Failed to load user details for email: {}" + userEmail + " - " + e);
            filterChain.doFilter(request, response);
            return;
        }
        if (!jwtService.validateToken(token, userDetails)) {
            log.error("JWT Token validation failed for user: {}" + userEmail);
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        log.info("✅ Authenticated user: {}" + userDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.info("User roles: {}" + userDetails.getAuthorities());
        filterChain.doFilter(request, response);
    }
}
