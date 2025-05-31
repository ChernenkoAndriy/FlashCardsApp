package com.example.application.repositories;

import com.example.application.data.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    List<Language> findAll();
}
