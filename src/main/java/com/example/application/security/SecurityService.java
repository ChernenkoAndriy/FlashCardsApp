package com.example.application.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    private final AuthenticationContext authenticationContext;

    public SecurityService(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    public Optional<UserDetails> getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class);
    }

    public boolean isUserLoggedIn() {
        return authenticationContext.isAuthenticated();
    }

    public boolean hasRole(String role) {
        return getAuthenticatedUser()
                .map(user -> user.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role)))
                .orElse(false);
    }

    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    public void logout() {
        authenticationContext.logout();
    }
}