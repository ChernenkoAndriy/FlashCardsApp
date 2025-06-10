package com.example.application.service;

import com.example.application.data.Deck;
import com.example.application.data.UserProgress;
import com.example.application.repositories.DeckRepository;
import com.example.application.repositories.UserProgressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProgressService {
    private final UserProgressRepository userProgressRepository;

    public UserProgressService(UserProgressRepository userProgressRepository) {
        this.userProgressRepository = userProgressRepository;
    }

    public List<UserProgress> findAll() {
        return userProgressRepository.findAll();
    }

    public void save(UserProgress userProgress) {
        userProgressRepository.save(userProgress);
    }

    public void delete(UserProgress userProgress) {
        userProgressRepository.delete(userProgress);
    }
}
