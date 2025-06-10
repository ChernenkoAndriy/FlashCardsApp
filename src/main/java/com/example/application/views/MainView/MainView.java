package com.example.application.views.MainView;

import com.example.application.data.Card;
import com.example.application.data.Deck;
import com.example.application.data.Language;
import com.example.application.dto.DeckDto;
import com.example.application.service.CardService;
import com.example.application.service.DeckService;
import com.example.application.service.LanguageService;
import com.example.application.service.UserService;
import com.example.application.security.SecurityService;
import com.example.application.views.Components.MainLayout;
import com.example.application.views.GameView.GameMode;
import com.example.application.views.MainView.Buttons.PlayJamButton;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.userdetails.UserDetails;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RolesAllowed({"ADMIN", "USER"})
@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | SLEEVE")
public class MainView extends VerticalLayout {

    private H1 greeting;
    private H2 allDecksTitle;
    private Button createDeckButton;
    private Accordion decksAccordion;

    private Map<String, List<DeckDto>> decksByLanguage = new HashMap<>();
    private final DeckService deckService;
    private final LanguageService languageService;
    private final CardService cardService;
    private final UserService userService;
    private final SecurityService securityService;
    private ComboBox<String> archivedSwitch = new ComboBox<>("Active switch");

    public MainView(DeckService deckService, LanguageService languageService,
                    CardService cardService, UserService userService, SecurityService securityService) {
        this.deckService = deckService;
        this.languageService = languageService;
        this.cardService = cardService;
        this.userService = userService;
        this.securityService = securityService;

        initialiseComponents();
        configureStyles();
        configureLayouts();
        add(greeting, createHeaderLayout(), createButtonLayout(), decksAccordion);
        updateDecksData(archivedSwitch.getValue());
        configureDeckLists(archivedSwitch.getValue());
        createDeckButton.addClickListener(e -> createDeck());
    }

    private void initialiseComponents() {
        greeting = new H1("Welcome, ! Let`s learn some words!");
        allDecksTitle = new H2("All Decks");
        createDeckButton = new Button("Create new deck");
        decksByLanguage = new HashMap<>();
        decksAccordion = new Accordion();
    }
    //" + securityService.getAuthenticatedUser().map(UserDetails::getUsername) + "

    private void configureStyles() {
        greeting.addClassName("banner");
        greeting.getStyle().set("font-size", "300%");
        greeting.getStyle().set("margin", "3rem");
        allDecksTitle.getStyle().set("margin", "0");
        createDeckButton.getStyle().set("font-size", "1.2rem");
        createDeckButton.addThemeName("primary");
        createDeckButton.setWidth("auto");
        createDeckButton.setHeight("100%");
        createDeckButton.getStyle().set("margin", "0");
        decksAccordion.setWidth("90%");
    }
    private void configureLayouts() {
        setHeight("100%");
        setAlignItems(Alignment.CENTER);
        setAlignSelf(Alignment.START, allDecksTitle);
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        getStyle().set("padding", "2rem 3rem 0rem 3rem");
    }
    private HorizontalLayout createHeaderLayout() {
        HorizontalLayout headerLayout = new HorizontalLayout(allDecksTitle);
        headerLayout.setAlignItems(Alignment.BASELINE);
        headerLayout.setWidth("90%");
        return headerLayout;
    }
    private HorizontalLayout createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout(createDeckButton);
        buttonLayout.setWidthFull();
        buttonLayout.addToEnd(archivedSwitch);
        archivedSwitch.setItems("Active", "Archived");
        buttonLayout.setAlignItems(Alignment.START);
        archivedSwitch.setAllowCustomValue(false);
        archivedSwitch.setValue("Active");
        archivedSwitch.addValueChangeListener(e -> {
        updateDecksData(archivedSwitch.getValue());
        configureDeckLists(archivedSwitch.getValue());});
        buttonLayout.getStyle()
                .set("width", "90%")
                .set("margin-left", "4rem");
        buttonLayout.setPadding(false);
        buttonLayout.setMargin(false);
        buttonLayout.getStyle().set("margin", "1rem 0");
        return buttonLayout;
    }
    private Integer getCurrentUserId() {
        return securityService.getAuthenticatedUser()
                .map(userDetails -> {
                    com.example.application.data.User user = userService.findByUsername(userDetails.getUsername());
                    return user != null ? user.getId() : null;
                })
                .orElse(null);
    }
    private void updateDecksData(String value) {
        decksByLanguage.clear();

        Integer userId = getCurrentUserId();
        if (userId == null) {
            System.err.println("User not authenticated!");
            return;
        }

        System.out.println("Current user ID: " + userId);

        for (Card card : cardService.findAllLearnedByUser(userId)) {
            System.out.println(card.getWord() + " " + card.getDeckId());
        }

        System.out.println(cardService.userHasCardsByLanguage(userId, 2));
        System.out.println(cardService.userHasUnlearnedCardsByLanguage(userId, 2));

        for (Card card : cardService.getJam(userId, 2)) {
            System.out.println(card.getWord() + " " + card.getDeckId() + " " + card.getId());
        }

        for (Language language : languageService.findLanguagesByUserId(userId)) {
            List<DeckDto> deckDtos = new ArrayList<>();
            if (value.equals("Active")) {
                deckDtos = deckService.findActiveDeckDtosByUserAndLanguage(userId, language.getId());
            }else {
                deckDtos = deckService.findArchivedDeckDtosByUserAndLanguage(userId, language.getId());
            }
            System.out.println("DECKS for language " + language.getName() + ": " + deckDtos.size());
            decksByLanguage.put(language.getName(), deckDtos);
        }
    }
    private void configureDeckLists(String value) {
        decksAccordion.getChildren().forEach(component -> decksAccordion.remove(component));

        for (Map.Entry<String, List<DeckDto>> entry : decksByLanguage.entrySet()) {
            String language = entry.getKey();
            List<DeckDto> languageDecks = entry.getValue();
            Integer languageId = languageService.findLanguageByName(language).getId();

            PlayJamButton playJamButton = new PlayJamButton(languageId, cardService, getCurrentUserId(), (langId, gameMode) -> {
                getUI().ifPresent(ui -> {
                    String langParam = URLEncoder.encode(langId.toString(), StandardCharsets.UTF_8);
                    String modeParam = URLEncoder.encode(gameMode.name(), StandardCharsets.UTF_8);
                    ui.navigate("game?deckId=" + languageId + "&mode=" + gameMode);
                });
            });


            VerticalLayout listLayout = new VerticalLayout();
            listLayout.setPadding(false);
            listLayout.setSpacing(false);
            listLayout.setWidthFull();

            listLayout.add(playJamButton);

            for (DeckDto deckDto : languageDecks) {
                DequeRow row;
                     row = new DequeRow(deckDto, this::deleteDeck, this::editDeck, this::playDeck, this::setActivness, value.equals("Active"));
                listLayout.add(row);
            }

            VerticalLayout wrapper = new VerticalLayout(listLayout);
            wrapper.setPadding(false);
            wrapper.setSpacing(false);


            var panel = decksAccordion.add(language, wrapper);
            panel.getElement().getStyle().set("min-height", "4rem");
        }
    }



    void deleteDeck(DeckDto deckDto) {
        // Додаткова перевірка безпеки - чи належить колода поточному користувачу
        Integer currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            System.err.println("Cannot delete deck: user not authenticated");
            return;
        }

        // Можна додати перевірку, чи належить колода користувачу
        // Deck deck = deckService.findById(deckDto.getId());
        // if (deck != null && deck.getUserId().equals(currentUserId)) {
        deckService.deleteByID(deckDto.getId());
        updateDecksData(archivedSwitch.getValue());
        configureDeckLists(archivedSwitch.getValue());
        // }
    }

    void setActivness(DeckDto deckDto, boolean activeness){
        // Logic to set the deck as inactive
        deckService.setDeckActiveStatus(deckDto.getId(), activeness);
        updateDecksData(archivedSwitch.getValue());
        configureDeckLists(archivedSwitch.getValue());
    }
    void editDeck(DeckDto deckDto) {
        getUI().ifPresent(ui -> ui.navigate("editor?deckId=" + deckDto.getId()));
    }
    void playDeck(DeckDto deckDto, GameMode gameMode) {
        getUI().ifPresent(ui -> {
            String deckId = URLEncoder.encode(deckDto.getId().toString(), StandardCharsets.UTF_8);
            String mode = URLEncoder.encode(gameMode.name(), StandardCharsets.UTF_8);
            ui.navigate("game?deckId=" + deckId + "&mode=" + mode);
        });
    }
    void createDeck() {
        Integer currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            System.err.println("Cannot create deck: user not authenticated");
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Create New Deck");

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.setWidth("350px");

        TextField nameField = new TextField("Deck Name");
        nameField.setWidthFull();

        ComboBox<Language> languageComboBox = new ComboBox<>("Language");
        languageComboBox.setItems(languageService.findAll());
        languageComboBox.setItemLabelGenerator(Language::getName);
        languageComboBox.setWidthFull();

        Button createBtn = new Button("Create", event -> {
            String name = nameField.getValue();
            Language selectedLanguage = languageComboBox.getValue();

            if (name != null && !name.isEmpty() && selectedLanguage != null) {
                Deck newDeck = new Deck();
                newDeck.setName(name);
                newDeck.setLanguageId(selectedLanguage.getId());
                // Якщо у вашій моделі Deck є поле userId, встановіть його:
                // newDeck.setUserId(currentUserId);

                deckService.save(newDeck);
                dialog.close();
                updateDecksData(archivedSwitch.getValue());
                configureDeckLists(archivedSwitch.getValue());
                System.out.println("Deck created. ID: " + newDeck.getId() + " for user: " + currentUserId);
                getUI().ifPresent(ui -> ui.navigate("editor?deckId=" + newDeck.getId()));
            } else {
                if (name == null || name.isEmpty())
                    nameField.setInvalid(true);
                if (selectedLanguage == null)
                    languageComboBox.setInvalid(true);
            }
        });

        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        layout.add(nameField, languageComboBox, createBtn);
        layout.expand(nameField, languageComboBox, createBtn);

        dialog.add(layout);
        dialog.setWidth("500px");
        dialog.setHeight("350px");
        dialog.open();
    }
}