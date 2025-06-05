package com.example.application.service;

import com.example.application.data.Card;
import com.example.application.data.User;
import com.example.application.repositories.CardRepository;
import com.example.application.repositories.UserRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public List<Card> findByDeckId(Integer deckId) {
        return cardRepository.findByDeckId(deckId);
    }

    public List<Card> findAllLearnedByUser(Integer userId) {
        return cardRepository.findAllLearnedByUser(userId);
    }

    public boolean userHasCardsByLanguage(Integer userId, Integer languageId) {
        return !cardRepository.findAllByUserByLanguage(userId, languageId).isEmpty();
    }

    public boolean userHasUnlearnedCardsByLanguage(Integer userId, Integer languageId) {
        return !cardRepository.findAllUnlearnedByUserByLanguage(userId, languageId).isEmpty();
    }

    public void decrementLearnedNumberForUsersWithLearnedCard(Integer cardId) {
        cardRepository.decrementLearnedNumberForUsersWithLearnedCard(cardId);
    }

    // Може бути ситуація, коли повертається порожній список. Це треба перевіряти вище і казати користувачу, що в нього нема карток на сьогодні
    public List<Card> getJam(Integer userId, Integer languageId) {
        List<Card> cardsForToday = cardRepository.findAllActiveNotLearnedByUserByLanguageWithDates(userId, languageId, LocalDateTime.parse("2025-06-06T10:00:00"));
        List<Card> cardsInLearn = new ArrayList<>();
        List<Card> freshCards = new ArrayList<>();

        int n = userRepository.findById(userId)
                .map(User::getWorkload)
                .orElse(0);

        if (cardsForToday.size() >= n) {
            cardsForToday = cardsForToday.subList(0, n);
        } else {
            /*cardsInLearn = cardRepository.findAllActiveLearningByUserByLanguageForToday(userId, languageId, LocalDateTime.parse("2025-06-06T10:00:00"));
            Collections.shuffle(cardsInLearn);
            if (cardsInLearn.size() >= n - cardsForToday.size()) {
                cardsInLearn = cardsInLearn.subList(0, n - cardsForToday.size());
            } else{*/
                freshCards = cardRepository.findAllActiveCreatedByUserByLanguage(userId, languageId);
                Collections.shuffle(freshCards);
                if (freshCards.size() >= n - (cardsForToday.size())) {
                    freshCards = freshCards.subList(0, n -(cardsForToday.size()));
                }
            //}
        }
        List<Card> jam = new ArrayList<>();
        jam.addAll(cardsForToday);
        jam.addAll(freshCards);
        Collections.shuffle(jam);
        return jam;
    }

    public void save(Card card) {
        cardRepository.save(card);
    }

    public void delete(Card card) {
        this.decrementLearnedNumberForUsersWithLearnedCard(card.getId());
        cardRepository.delete(card);
    }

}