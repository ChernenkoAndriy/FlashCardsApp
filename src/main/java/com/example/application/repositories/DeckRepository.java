package com.example.application.repositories;

import com.example.application.data.Deck;
import com.example.application.data.Language;
import com.example.application.dto.DeckDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Integer> {

  List<Deck> findAll();

  /*@Query("""
    SELECT DISTINCT d FROM Deck d
    JOIN Card c ON c.deckId = d.id
    JOIN UserProgress p ON p.cardId = c.id
    WHERE p.userId = :userId AND d.isActive = true AND d.languageId=:languageId
    """)
  List<Deck> findActiveDecksByUserAndLanguage(@Param("userId") Integer userId, @Param("languageId") Integer languageId);*/

  @Query("""
    SELECT DISTINCT new com.example.application.dto.DeckDto(d.id, d.name, d.cardsNumber, l.name, ud.progress)
    FROM Deck d
    JOIN Card c ON c.deckId = d.id
    JOIN UserProgress p ON p.cardId = c.id
    JOIN Language l ON l.id = d.languageId
    JOIN UserDeck ud ON d.id = ud.deckId
    WHERE ud.userId = :userId AND p.userId = :userId AND ud.isActive = true AND d.languageId=:languageId
""")
  List<DeckDto> findActiveDeckDtosByUserAndLanguage(@Param("userId") Integer userId, @Param("languageId") Integer languageId);

}
