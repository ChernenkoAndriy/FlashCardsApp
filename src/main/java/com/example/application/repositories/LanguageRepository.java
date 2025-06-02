package com.example.application.repositories;

import com.example.application.data.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    List<Language> findAll();

    @Query("""
    SELECT DISTINCT l FROM Language l
    JOIN Deck d ON d.languageId = l.id
    JOIN Card c ON c.deckId = d.id
    JOIN UserProgress p ON p.cardId = c.id
    WHERE p.userId = :userId
    """)
    List<Language> findLanguagesByUserId(@Param("userId") Integer userId);
}
