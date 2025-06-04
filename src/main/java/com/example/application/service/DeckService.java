package com.example.application.service;


import com.example.application.data.Deck;
import com.example.application.dto.DeckDto;
import com.example.application.repositories.DeckRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeckService {

    private final DeckRepository deckRepository;

    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public List<Deck> findAll() {
        return deckRepository.findAll();
    }

    public List<Deck> findActiveDecksByUserAndLanguage(@Param("userId") Integer userId, @Param("languageId") Integer languageId) {
        return deckRepository.findActiveDecksByUserAndLanguage(userId, languageId);
    }

    public List<DeckDto> findActiveDeckDtosByUserAndLanguage(@Param("userId") Integer userId, @Param("languageId") Integer languageId) {
        return deckRepository.findActiveDeckDtosByUserAndLanguage(userId, languageId);
    }

    public List<Deck> findActiveByLanguageId(Integer languageId) {
        return deckRepository.findByLanguageIdAndIsActiveTrue(languageId);
    }

    public void save(Deck deck) {
        deckRepository.save(deck);
    }

    public void delete(Deck deck) {
        deckRepository.delete(deck);
    }

    public void deleteByID(Integer id) {
        deckRepository.deleteById(id);
    }

    public Optional<Deck> findById(Integer deckId) {
        return deckRepository.findById(deckId);
    }
}
