package com.example.application.service;

import com.example.application.data.User;
import com.example.application.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String email, String rawPassword, String language, int workload) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setLanguage(language);
        user.setWorkload(workload);
        user.setRole(User.Role.USER);

        return userRepository.save(user);
    }

    public User createAdmin(String username, String email, String rawPassword, String language, int workload) {
        User admin = new User();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setLanguage(language);
        admin.setWorkload(workload);
        admin.setRole(User.Role.ADMIN);

        return userRepository.save(admin);
    }
    public User save(User user) {
        return createUser(user.getUsername(), user.getEmail(), user.getPassword(), user.getLanguage(), user.getWorkload());
    }
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public User findByUsername(String username) {
       return userRepository.findByUsername(username);
    }
}