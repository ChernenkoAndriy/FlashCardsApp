package com.example.application.views.MainView;

import com.example.application.data.Deck;
import com.example.application.data.Language;
import com.example.application.data.User;
import com.example.application.dto.DeckDto;
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
    //кнопка для створення колод
    private Button createDeckButton;
    //якщо коротко то це ui компонент, який організовує вкладки, в які можна пхати ряди з деками, або що завгодно
    private Accordion decksAccordion;

    //це потрібно чисто для прикладу щоб наповнити сторінку, але подібну структуру можна використати і з сервісом
    //це хешмап типу (мова -> список колод по всій мові)
    //можна спробувати написати спосіб отримання подібної мапи з бази і буде метод для відображення цього
    private Map<String, List<DequeCard>> decksByLanguage;

    //доданий через ін'єкцію сервіс, щоб використовувати його як поле сторінки
    private final DeckService deckService;
    private final UserService userService;
    private final LanguageService languageService;


    public MainView(DeckService deckService, UserService userService, LanguageService languageService) {
        this.deckService = deckService;
        this.userService = userService;
        this.languageService = languageService;
        initialiseComponents();
        configureStyles();
        configureLayouts();
        configureData();
        configureDeckLists();

        add(greeting, createHeaderLayout(), createButtonLayout(), decksAccordion);
    }
    //ось типу наповнення колекцій
    private void configureData() {
        System.out.println();
        for(Language language: languageService.findLanguagesByUserId(userService.findAll().get(0).getId())){
            System.out.println("Language: " + language.getName());
            decksByLanguage.put(language.getName(), new ArrayList<>());
            for(DeckDto deck: deckService.findActiveDeckDtosByUserAndLanguage(userService.findAll().get(0).getId(),language.getId())){
                System.out.println(deck.getLanguageName()+" "+ deck.getId()+" "+ deck.getCardsNumber());
                decksByLanguage.get(language.getName()).add(new DequeCard(deck));
            }
        }
    }
    //ось метод для відображення таких речей

    private void configureDeckLists() {
        for (Map.Entry<String, List<DequeCard>> entry : decksByLanguage.entrySet()) {
            String language = entry.getKey();
            List<DequeCard> languageDecks = entry.getValue();

            VerticalLayout listLayout = new VerticalLayout();
            listLayout.setPadding(false);
            listLayout.setSpacing(false);
            listLayout.setWidthFull();

            for (DequeCard dequeCard : languageDecks) {
                //було б непогано якщо в конструктор deckrow передавалась дека а не готова картка, і компонент автоматично ініціалізувався
                //також треба зробити назначення дій на кнопки, було б добре передати їх лямбдою
                DequeRow row = new DequeRow(dequeCard);
                listLayout.add(row);
            }

            VerticalLayout wrapper = new VerticalLayout(listLayout);
            wrapper.setPadding(false);
            wrapper.setSpacing(false);

            var panel = decksAccordion.add(language, wrapper);
            panel.getElement().getStyle().set("min-height", "4rem");
        }
    }

    private void initialiseComponents() {
        greeting = new H1("Welcome! Let`s learn some words!");
        allDecksTitle = new H2("All Decks");
        createDeckButton = new Button("Create new deck");
        decksByLanguage = new HashMap<>();
        decksAccordion = new Accordion();
    }

    //ОСЬ ВСЕ ЩО ДАЛІ ЦЕ ТУПО СТИЛІЗАЦІЯ ЗОВНІШНЬОГО ВИГЛЯДУ, ТУДИ МОЖНА НЕ ЛІЗТИ
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
}
