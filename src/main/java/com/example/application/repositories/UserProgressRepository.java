package com.example.application.repositories;

import com.example.application.data.Deck;
import com.example.application.data.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Integer> {
    List<UserProgress> findAll();

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO user_progress (user_id, card_id,  period, is_correct, date, is_learned)
            SELECT ud.user_id, :cardId, 'created', 0, CURRENT_TIMESTAMP, 0
            FROM user_deck ud
            JOIN card c ON ud.deck_id = c.deck_id
            WHERE c.id = :cardId
            
            """, nativeQuery = true)
    void updateProgresses(Integer cardId, Integer deckId);

//    public void nextStageForCardById(@Param("userId") Integer userId, @Param("cardId") Integer cardId);

}
