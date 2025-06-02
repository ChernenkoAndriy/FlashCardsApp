package com.example.application.service;

import com.example.application.data.Card;
import com.example.application.repositories.CardRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public List<Card> findByDeckId(Integer deckId) {
        return cardRepository.findByDeckId(deckId);
    }

    public void save(Card card) {
        cardRepository.save(card);
    }

    public void delete(Card card) {
        cardRepository.delete(card);
    }

    public void update(Card card) {
        //TODO update method
    }
}