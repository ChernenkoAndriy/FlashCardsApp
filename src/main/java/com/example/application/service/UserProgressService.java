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

    public void playedCard(Integer cardId, Integer userId, boolean userKnowsCard) {
        //TODO
        // знайти period цієї картки
        // знайти колоду
        // switch(userKnowsCard + period) параметри в запит до зміни в user_progress: next_date, period
        //                                параметри в запит до зміни в user_deck_progress: % вивчення колоди
    }

    public void save(UserProgress userProgress) {
        userProgressRepository.save(userProgress);
    }

    public void delete(UserProgress userProgress) {
        userProgressRepository.delete(userProgress);
    }
}
