package com.example.application.service;

import com.example.application.data.Card;
import com.example.application.data.User;
import com.example.application.data.UserProgress;
import com.example.application.dto.CardDto;
import com.example.application.repositories.CardRepository;
import com.example.application.repositories.UserProgressRepository;
import com.example.application.repositories.UserRepository;
import com.example.application.views.GameView.GameMode;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final UserProgressRepository userProgressRepository;

    public CardService(CardRepository cardRepository, UserRepository userRepository, UserProgressRepository userProgressRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.userProgressRepository = userProgressRepository;
    }

    public Optional<Card> findById(Integer id) {
        return cardRepository.findById(id);
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
                freshCards = freshCards.subList(0, n - (cardsForToday.size()));
            }
            //}
        }
        List<Card> jam = new ArrayList<>();
        jam.addAll(cardsForToday);
        jam.addAll(freshCards);
        Collections.shuffle(jam);
        return jam;
    }

    public void markGuessed(GameMode gameMode, Card card, Integer userId) {
        UserProgress userProgress = userProgressRepository.findByUserIdAndCardId(userId, card.getId());
        String period = userProgress.getPeriod();
        //TODO: update according to levels
        switch (period) {
            case "created", "learning":{
                if (gameMode.equals(GameMode.REVISION)){
                    userProgressRepository.updatePeriod(card.getId(), userId, LocalDateTime.now().plusDays(1), "first");
                } else if (gameMode.equals(GameMode.DEFINITIONS)){
                    userProgressRepository.updatePeriod(card.getId(), userId, LocalDateTime.now().plusDays(3), "second");
                } else if (gameMode.equals(GameMode.SENTENCES)){
                    userProgressRepository.updatePeriod(card.getId(), userId, LocalDateTime.now().plusDays(7), "third");
                } /*else if (gameMode.equals(GameMode.USAGE)){
                    userProgressRepository.setLearned(card.getId(), userId);
                }*/
                break;
            }
            case "first":{
                if (gameMode.equals(GameMode.DEFINITIONS)){
                    userProgressRepository.updatePeriod(card.getId(), userId, LocalDateTime.now().plusDays(3), "second");
                } else if (gameMode.equals(GameMode.SENTENCES)){
                    userProgressRepository.updatePeriod(card.getId(), userId, LocalDateTime.now().plusDays(7), "third");
                } /*else if (gameMode.equals(GameMode.USAGE)){
                    userProgressRepository.setLearned(card.getId(), userId);
                }*/
                break;
            }
            case "second":{
                if (gameMode.equals(GameMode.SENTENCES)){
                    userProgressRepository.updatePeriod(card.getId(), userId, LocalDateTime.now().plusDays(7), "third");
                } /*else if (gameMode.equals(GameMode.USAGE)){
                    userProgressRepository.setLearned(card.getId(), userId);
                }*/
                //TODO коли буде режим 4
                break;
            }
            case "third":{
                /* if (gameMode.equals(GameMode.USAGE)){
                    userProgressRepository.setLearned(card.getId(), userId);
                }*/
                //TODO коли буде режим 4
                break;
            }
        }
    }

    public void markLearning(Card card, Integer userId) {
        UserProgress userProgress = userProgressRepository.findByUserIdAndCardId(userId, card.getId());
        if(Objects.equals(userProgress.getPeriod(), "created")){
            userProgressRepository.updatePeriod(card.getId(), userId, LocalDateTime.now().plusDays(1), "learning");
        }

    }

    public List<CardDto> findCardDtosByUserAndDeckId(@Param("userId") Integer userId, @Param("deckId") Integer deckId){
        return cardRepository.findCardDtosByUserAndDeckId(userId, deckId);
    }


    public void save(Card card) {
        cardRepository.save(card);
        System.out.println("inserted card with id: " + card.getId());
        this.insertUserProgressForNewCard(card.getId(), card.getDeckId());
    }

    public void insertUserProgressForNewCard(Integer cardId, Integer deckId) {
        Optional<UserProgress> presentCard = userProgressRepository.findByCardId(cardId);
        if (!presentCard.isPresent()) {
            List<Integer> userIds = userRepository.findUserIdsByDeckId(deckId);

            List<UserProgress> progresses = userIds.stream().map(userId -> {
                UserProgress up = new UserProgress();
                up.setUserId(userId);
                up.setCardId(cardId);
                up.setPeriod("created");
                up.setDate(LocalDateTime.now());
                return up;
            }).toList();

            userProgressRepository.saveAll(progresses);
        }
    }

    public void delete(Card card) {
        this.decrementLearnedNumberForUsersWithLearnedCard(card.getId());
        cardRepository.delete(card);
    }

}