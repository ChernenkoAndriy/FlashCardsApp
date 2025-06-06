package com.example.application.repositories;

import com.example.application.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Query("SELECT ud.userId FROM UserDeck ud WHERE ud.deckId = :deckId")
    List<Integer> findUserIdsByDeckId(@Param("deckId") Integer deckId);
}

