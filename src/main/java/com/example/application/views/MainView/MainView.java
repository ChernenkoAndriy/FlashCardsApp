package com.example.application.views.MainView;

import com.example.application.data.Card;
import com.example.application.data.Language;
import com.example.application.dto.DeckDto;
import com.example.application.service.CardService;
import com.example.application.service.DeckService;
import com.example.application.service.LanguageService;
import com.example.application.service.UserService;
import com.example.application.views.Components.MainLayout;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

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
    private final UserService userService;
    private final LanguageService languageService;
    private final CardService cardService;


    public MainView(DeckService deckService, UserService userService, LanguageService languageService, CardService cardService) {
        this.deckService = deckService;
        this.userService = userService;
        this.languageService = languageService;
        this.cardService = cardService;

        initialiseComponents();
        configureStyles();
        configureLayouts();

        // Завантаження і показ колод
        updateDecksData();
        configureDeckLists();

        add(greeting, createHeaderLayout(), createButtonLayout(), decksAccordion);

        // Прив’язуємо кнопку створення нової колоди
        createDeckButton.addClickListener(e -> createDeck());
    }
    private void initialiseComponents() {
        greeting = new H1("Welcome! Let`s learn some words!");
        allDecksTitle = new H2("All Decks");
        createDeckButton = new Button("Create new deck");
        decksByLanguage = new HashMap<>();
        decksAccordion = new Accordion();
    }
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
        buttonLayout.setAlignItems(Alignment.START);
        buttonLayout.getStyle()
                .set("width", "90%")
                .set("margin-left", "4rem");
        buttonLayout.setPadding(false);
        buttonLayout.setMargin(false);
        buttonLayout.getStyle().set("margin", "1rem 0");
        return buttonLayout;
    }
    private void updateDecksData() {
        decksByLanguage.clear();

        for(Card card : cardService.findAllLearnedByUser(1)){
            System.out.println(card.getWord()+" "+card.getDeckId());
        }

        for (Card card:cardService.getJam(1,3)){
            System.out.println(card.getWord()+" "+card.getDeckId()+" "+card.getId());
        }
        Integer userId = userService.findAll().get(0).getId();
        for (Language language : languageService.findLanguagesByUserId(userId)) {
            List<DeckDto> deckDtos = deckService.findActiveDeckDtosByUserAndLanguage(userId, language.getId());
            decksByLanguage.put(language.getName(), deckDtos);
        }
    }
    private void configureDeckLists() {
        decksAccordion.getChildren().forEach(component -> decksAccordion.remove(component));

        for (Map.Entry<String, List<DeckDto>> entry : decksByLanguage.entrySet()) {
            String language = entry.getKey();
            List<DeckDto> languageDecks = entry.getValue();

            VerticalLayout listLayout = new VerticalLayout();
            listLayout.setPadding(false);
            listLayout.setSpacing(false);
            listLayout.setWidthFull();

            for (DeckDto deckDto : languageDecks) {
                DequeRow row = new DequeRow(deckDto, this::deleteDeck, this::editDeck, this::playDeck);
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
        deckService.deleteByID(deckDto.getId());
        updateDecksData();
        configureDeckLists();
    }
    void editDeck(DeckDto deckDto) {
        getUI().ifPresent(ui -> ui.navigate("editor?deckId=" + deckDto.getId()));
    }
    void playDeck(DeckDto deckDto) {

    }
    void createDeck() {
        updateDecksData();
        configureDeckLists();
    }
}
