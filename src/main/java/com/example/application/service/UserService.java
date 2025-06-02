package com.example.application.service;

import com.example.application.data.Deck;
import com.example.application.data.User;
import com.example.application.data.UserProgress;
import com.example.application.repositories.DeckRepository;
import com.example.application.repositories.UserProgressRepository;
import com.example.application.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}