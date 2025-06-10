package com.example.application.repositories;

import com.example.application.data.Deck;
import com.example.application.data.Language;
import com.example.application.dto.DeckDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
              SELECT DISTINCT new com.example.application.dto.DeckDto(d.id, d.name, d.cardsNumber, l.name, ud.progress, ud.learnedNumber)
              FROM Deck d
              JOIN Language l ON l.id = d.languageId
              JOIN UserDeck ud ON d.id = ud.deckId
              WHERE ud.userId = :userId AND ud.isActive = true AND d.languageId=:languageId
          """)
  List<DeckDto> findActiveDeckDtosByUserAndLanguage(@Param("userId") Integer userId, @Param("languageId") Integer languageId);


  @Modifying
  @Transactional
  @Query(value = "UPDATE user_deck SET is_active = :isActive WHERE deck_id = :deckId", nativeQuery = true)
  void updateUserDeckActiveStatus(@Param("deckId") Integer deckId, @Param("isActive") boolean isActive);

}