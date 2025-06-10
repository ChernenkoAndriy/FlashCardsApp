
package com.example.application.repositories;

import com.example.application.data.Card;
import com.example.application.dto.DeckDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.example.application.dto.CardDto;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findByDeckId(Integer deckId);

    @Query("""
                SELECT c
                FROM Card c
                WHERE EXISTS (
                    SELECT 1
                    FROM UserProgress u
                    WHERE u.cardId =c.id AND u.isLearned = TRUE AND u.userId = :userId)
            """)
    List<Card> findAllLearnedByUser(Integer userId);

    @Query("""
                SELECT c
                FROM Card c
                JOIN Deck d ON c.deckId=d.id
                WHERE EXISTS (
                    SELECT 1
                    FROM UserProgress u
                    WHERE u.cardId =c.id AND u.userId = :userId)
                  AND d.languageId = :languageId
            """)
    List<Card> findAllByUserByLanguage(Integer userId, Integer languageId);

    @Query("""
                SELECT c
                FROM Card c
                JOIN Deck d ON c.deckId=d.id
                WHERE EXISTS (
                    SELECT 1
                    FROM UserProgress u
                    WHERE u.cardId =c.id AND u.userId = :userId AND u.isLearned)
                  AND d.languageId = :languageId
            """)
    List<Card> findAllUnlearnedByUserByLanguage(Integer userId, Integer languageId);


    @Modifying
    @Transactional
    @Query("""
                UPDATE UserDeck ud
                SET ud.learnedNumber = ud.learnedNumber - 1
                WHERE ud.userId IN (
                    SELECT up.userId
                    FROM UserProgress up
                    WHERE up.cardId = :cardId AND up.isLearned = TRUE
                )
                AND ud.deckId = (
                    SELECT c.deckId
                    FROM Card c
                    WHERE c.id = :cardId
                )
            """)
    void decrementLearnedNumberForUsersWithLearnedCard(@Param("cardId") Integer cardId);

    @Query("""
            SELECT c
            FROM Card c
            JOIN Deck d ON d.id = c.deckId
            JOIN UserProgress p ON p.cardId = c.id
            JOIN UserDeck ud ON c.deckId = ud.deckId
            WHERE ud.userId = :userId AND p.userId = :userId AND ud.isActive = true AND d.languageId=:languageId AND p.isLearned = false AND p.nextDate IS NOT NULL AND p.nextDate<=:today
            ORDER BY p.nextDate ASC
            """)
    List<Card> findAllActiveNotLearnedByUserByLanguageWithDates(@Param("userId") Integer userId, @Param("languageId") Integer languageId, @Param("today") LocalDateTime date);

    @Query("""
            SELECT c
            FROM Card c
            JOIN Deck d ON d.id = c.deckId
            JOIN UserProgress p ON p.cardId = c.id
            JOIN UserDeck ud ON c.deckId = ud.deckId
            WHERE ud.userId = :userId AND p.userId = :userId AND ud.isActive = true AND d.languageId=:languageId AND p.isLearned = false AND p.period='learning' AND p.nextDate IS NOT NULL AND p.nextDate<=:today
            """)
    List<Card> findAllActiveLearningByUserByLanguageForToday(@Param("userId") Integer userId, @Param("languageId") Integer languageId, @Param("today") LocalDateTime date);


    @Query("""
            SELECT c
            FROM Card c
            JOIN Deck d ON d.id = c.deckId
            JOIN UserProgress p ON p.cardId = c.id
            JOIN UserDeck ud ON c.deckId = ud.deckId
            WHERE ud.userId = :userId AND p.userId = :userId AND ud.isActive = true AND d.languageId=:languageId AND p.isLearned = false AND p.period='created'
            """)
    List<Card> findAllActiveCreatedByUserByLanguage(@Param("userId") Integer userId, @Param("languageId") Integer languageId);


    @Query("""
            SELECT c
            FROM Card c
            WHERE c.id = :id
            """)
    Optional<Card> findById(Integer id);


    @Query("""
                SELECT DISTINCT new com.example.application.dto.CardDto(c.id, c.word, c.translate, c.definition, c.image, p.period, p.isLearned)
                FROM Card c
                JOIN UserProgress p ON p.cardId = c.id
                WHERE p.userId = :userId AND c.deckId = :deckId
            """)
    List<CardDto> findCardDtosByUserAndDeckId(@Param("userId") Integer userId, @Param("deckId") Integer deckId);



}
