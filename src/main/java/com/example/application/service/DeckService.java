package com.example.application.service;


import com.example.application.data.Card;
import com.example.application.data.Deck;
import com.example.application.data.UserDeck;
import com.example.application.data.UserProgress;
import com.example.application.dto.DeckDto;
import com.example.application.repositories.DeckRepository;
import com.example.application.repositories.UserDeckRepository;
import com.example.application.repositories.UserProgressRepository;
import com.example.application.security.SecurityService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeckService {

    private final DeckRepository deckRepository;
    private final UserDeckRepository userDeckRepository;
    private final SecurityService securityService;
    private final UserService userService;



    public DeckService(DeckRepository deckRepository, UserDeckRepository userDeckRepository, SecurityService securityService, UserService userService) {
        this.deckRepository = deckRepository;
        this.userDeckRepository = userDeckRepository;
        this.securityService = securityService;
        this.userService = userService;
    }

    public void setDeckActiveStatus(Integer deckId, boolean isActive) {
        deckRepository.updateUserDeckActiveStatus(deckId, isActive);
    }


    public List<Deck> findAll() {
        return deckRepository.findAll();
    }

    /*public List<Deck> findActiveDecksByUserAndLanguage(@Param("userId") Integer userId, @Param("languageId") Integer languageId) {
        return deckRepository.findActiveDecksByUserAndLanguage(userId, languageId);
    }*/

    public List<DeckDto> findActiveDeckDtosByUserAndLanguage(@Param("userId") Integer userId, @Param("languageId") Integer languageId) {
        return deckRepository.findActiveDeckDtosByUserAndLanguage(userId, languageId);
    }

    public List<DeckDto> findNotActiveDeckDtosByUserAndLanguage(@Param("userId") Integer userId, @Param("languageId") Integer languageId) {
        return deckRepository.findAllNotActiveDeckDtosByUser(userId);
    }


    public void save(Deck deck) {
        deckRepository.save(deck);
        this.insertUserDeckForNewDeck(deck.getId(), securityService.getAuthenticatedUser()
                .map(userDetails -> {
                    com.example.application.data.User user = userService.findByUsername(userDetails.getUsername());
                    return user != null ? user.getId() : null;
                })
                .orElse(null));
    }

    public void delete(Deck deck) {
        deckRepository.delete(deck);
    }

    public void deleteByID(Integer id) {
        deckRepository.deleteById(id);
    }

    public Optional<Deck> findById(Integer deckId) {
        return deckRepository.findById(deckId);
    }

    public void insertUserDeckForNewDeck(Integer deckId, Integer userId) {
        Optional<UserDeck> presentDeck = userDeckRepository.findById(deckId);
        System.out.println(1);
        if (!presentDeck.isPresent()) {
            System.out.println(2);
            UserDeck ud = new UserDeck();
            ud.setUserId(userId);
            ud.setDeckId(deckId);
            ud.setLearnedNumber(0);
            ud.setProgress(0);
            ud.setActive(true);
            System.out.println("Inserting new user deck: " + ud.getDeckId() + " for user: " + ud.getUserId());
            userDeckRepository.save(ud);
        }
    }
}
