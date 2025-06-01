package com.example.application.service;


import com.example.application.data.Deck;
import com.example.application.repositories.CardRepository;
import com.example.application.repositories.DeckRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {

    private final DeckRepository deckRepository;

    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public List<Deck> findAll() {
        return deckRepository.findAll();
    }

    public List<Deck> findActiveByLanguageId(Long languageId) {
        return deckRepository.findByLanguageIdAndIsActiveTrue(languageId);
    }

    public void save(Deck deck) {
        deckRepository.save(deck);
    }

    public void delete(Deck deck) {
        deckRepository.delete(deck);
    }
}
