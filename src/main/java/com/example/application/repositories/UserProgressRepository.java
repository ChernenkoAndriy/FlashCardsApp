package com.example.application.repositories;

import com.example.application.data.Deck;
import com.example.application.data.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Integer> {
    List<UserProgress> findAll();

    public void nextStageForCardById(@Param("userId") Integer userId, @Param("cardId") Integer cardId);

}
