package com.example.application.repositories;

import com.example.application.data.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

  List<Deck> findByDeckId(Long deckId);

}
