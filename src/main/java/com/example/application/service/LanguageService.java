package com.example.application.service;


import com.example.application.data.Language;
import org.springframework.stereotype.Service;
import com.example.application.repositories.LanguageRepository;
import java.util.List;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public List<Language> findAll() {
        return languageRepository.findAll();
    }
    public void save(Language language) {
        languageRepository.save(language);
    }
    public void delete(Language language) {
        languageRepository.delete(language);
    }

}
