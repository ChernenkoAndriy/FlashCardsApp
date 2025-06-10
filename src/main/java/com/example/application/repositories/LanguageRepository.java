package com.example.application.repositories;

import com.example.application.data.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    List<Language> findAll();

    @Query("""
    SELECT DISTINCT l FROM Language l
    JOIN Deck d ON d.languageId = l.id
    JOIN UserDeck ud ON ud.deckId = d.id
    WHERE ud.userId = :userId
    """)
    List<Language> findLanguagesByUserId(@Param("userId") Integer userId);
}
