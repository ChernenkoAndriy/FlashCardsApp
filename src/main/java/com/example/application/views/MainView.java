package com.example.application.views;

import com.example.application.views.Components.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ADMIN", "USER"})
@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | SLEEVE")
public class MainView extends VerticalLayout {

    private final H1 greeting = new H1("Welcome! Let`s learn some words!");
    private final VerticalLayout allDecks = new VerticalLayout();

    public MainView() {
        greeting.addClassName("banner");
        greeting.getStyle().set("font-size", "300%");
        setAlignItems(Alignment.CENTER);
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        getStyle().set("padding", "2rem 3rem 2rem 3rem"); // top right bottom left

        // All Decks
        H2 allDecksTitle = new H2("All Decks");
        allDecks.setSpacing(true);
        allDecks.setPadding(true);
        allDecks.setWidthFull();

        // Add sample cards to all decks
        for (int i = 1; i <= 5; i++) {
            Card card = new Card();
            card.add(new Text("Deck " + i));
            card.getStyle().set("padding", "1rem").set("margin", "0.5rem");
            allDecks.add(card);
        }

        add(greeting, allDecksTitle, allDecks);
    }
}
