package com.example.application.views.MainView;

import com.example.application.views.Components.MainLayout;
import com.example.application.views.MainView.Buttons.DeleteButton;
import com.example.application.views.MainView.Buttons.EditButton;
import com.example.application.views.MainView.Buttons.PlayButton;
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
    private Button createDeckButton;
    private Map<String, List<DequeCard>> decksByLanguage;
    private Accordion decksAccordion;
    public MainView() {
        initialiseComponents();
        configureStyles();
        configureLayouts();
        configureData();
        configureDeckLists();

        add(greeting, createHeaderLayout(), createButtonLayout(), decksAccordion);
    }
    private void configureData() {
        decksByLanguage.put("English", new ArrayList<>());
        decksByLanguage.put("Ukrainian", new ArrayList<>());
        decksByLanguage.put("German", new ArrayList<>());

        for (int i = 1; i <= 10; i++) {
            decksByLanguage.get("English").add(new DequeCard("English Deck " + i));
            decksByLanguage.get("Ukrainian").add(new DequeCard("Ukrainian Deck " + i));
            decksByLanguage.get("German").add(new DequeCard("German Deck " + i));
        }
    }
    private void configureDeckLists() {
        for (Map.Entry<String, List<DequeCard>> entry : decksByLanguage.entrySet()) {
            String language = entry.getKey();
            List<DequeCard> languageDecks = entry.getValue();

            VerticalLayout listLayout = new VerticalLayout();
            listLayout.setPadding(false);
            listLayout.setSpacing(false);
            listLayout.setWidthFull();

            for (DequeCard dequeCard : languageDecks) {
                HorizontalLayout layout = new HorizontalLayout();
                layout.setWidthFull();
                layout.setAlignItems(Alignment.CENTER);

                dequeCard.setWidth("60%");
                layout.add(dequeCard);

                Button deleteButton = new DeleteButton();
                Button editButton = new EditButton();
                Button playButton = new PlayButton();

                layout.addToEnd(deleteButton, editButton, playButton);

                listLayout.add(layout);
            }

            VerticalLayout wrapper = new VerticalLayout(listLayout);
            wrapper.setPadding(false);
            wrapper.setSpacing(false);

            var panel = decksAccordion.add(language, wrapper);
            panel.getElement()
                    .getStyle()
                    .set("min-height", "4rem");
        }
    }


    private void initialiseComponents() {
        greeting = new H1("Welcome! Let`s learn some words!");
        allDecksTitle = new H2("All Decks");
        createDeckButton = new Button("Create new deck");
        decksByLanguage = new HashMap<>();
        decksAccordion = new Accordion();
    }
    private void configureStyles() {
        // Greeting style
        greeting.addClassName("banner");
        greeting.getStyle().set("font-size", "300%");
        greeting.getStyle().set("margin", "3rem");

        // allDecksTitle style
        allDecksTitle.getStyle().set("margin", "0");

        // Create deck button style
        createDeckButton.getStyle().set("font-size", "1.2rem");
        createDeckButton.addThemeName("primary");
        createDeckButton.setWidth("auto");
        createDeckButton.setHeight("100%");
        createDeckButton.getStyle().setMargin("0");

        // Accordion width
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
                .setMarginLeft("4rem");
        buttonLayout.setPadding(false);
        buttonLayout.setMargin(false);
        buttonLayout.getStyle().set("margin", "1rem 0");
        return buttonLayout;
    }

}
