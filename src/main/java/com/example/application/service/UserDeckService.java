package com.example.application.service;

import com.example.application.data.Deck;
import com.example.application.data.UserDeck;
import com.example.application.data.UserProgress;
import com.example.application.repositories.UserDeckRepository;
import com.example.application.repositories.UserProgressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDeckService {
    private final UserDeckRepository userDeckRepository;

    public UserDeckService(UserDeckRepository userDeckRepository) {
        this.userDeckRepository = userDeckRepository;
    }

    public List<UserDeck> findAll() {
        return userDeckRepository.findAll();
    }

    public void save(UserDeck userDeck) {
        userDeckRepository.save(userDeck);
    }

    public void delete(UserDeck userDeck) {
        userDeckRepository.delete(userDeck);
    }
}