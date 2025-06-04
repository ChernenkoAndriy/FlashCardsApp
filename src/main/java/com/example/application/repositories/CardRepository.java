package com.example.application.repositories;

import com.example.application.data.Card;
import com.example.application.dto.DeckDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findByDeckId(Integer deckId);

    @Query("""
    SELECT c
    FROM Card c
    WHERE EXISTS (
        SELECT 1
        FROM UserProgress u
        WHERE u.cardId =c.id AND u.isLearned = TRUE)    
""")
    List<Card> findAllLearned();


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

}