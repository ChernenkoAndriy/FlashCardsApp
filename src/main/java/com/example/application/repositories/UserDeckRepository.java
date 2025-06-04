package com.example.application.repositories;

import com.example.application.data.UserDeck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDeckRepository extends JpaRepository<UserDeck, Integer> {
    List<UserDeck> findAll();


}
