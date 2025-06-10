package com.example.application.repositories;

import com.example.application.data.UserDeck;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDeckRepository extends JpaRepository<UserDeck, Integer> {
    List<UserDeck> findAll();

    @Modifying
    @Transactional
    @Query("""
            UPDATE UserDeck ud
            SET ud.progress = :newPercentage
            WHERE ud.deckId = :deckId AND ud.userId = :userId
            """)
    void updatePercentage(@Param("deckId") Integer deckId, @Param("userId") Integer userId, @Param("newPercentage") Integer newPercentage);
    //ud.learnedNumber / (SELECT d.cardsNumber FROM Deck d WHERE d.id = :deckId)

    Optional<UserDeck> findUserDeckByDeckIdAndUserId(Integer deckId, Integer userId);

}
