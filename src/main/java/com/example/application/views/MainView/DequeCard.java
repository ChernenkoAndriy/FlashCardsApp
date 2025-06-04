package com.example.application.views.MainView;

import com.example.application.data.Deck;
import com.example.application.dto.DeckDto;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

//тут треба зробити щоб клас створювався від об'єкту деки і все
//він наслідується від ваадін картки і показує на сторінці фіолетовий квадрат з декою
public class DequeCard extends Card {
    private final H3 name = new H3();
    private final ProgressBar progressBar = new ProgressBar();
    private final H5 size = new H5();
    private final H5 mainLanguage = new H5();

    public DequeCard(String title, Integer numberOfCards, String language, Integer progress) {
        name.setText(title);
        size.setText("Size: " + numberOfCards);
        mainLanguage.setText("Language: " + language);
        this.setProgress(progress);

        VerticalLayout left = createLeftLayout();
        VerticalLayout center = createCenterLayout();
        VerticalLayout right = createRightLayout();

        HorizontalLayout layout = new HorizontalLayout(left, center, right);
        configureMainLayout(layout);

        add(layout);

        applyCardStyles();
        applyTextColors();
    }

    public DequeCard(DeckDto deckFromDB) {
        this(deckFromDB.getName(), deckFromDB.getCardsNumber(), deckFromDB.getLanguageName(), deckFromDB.getProgress());
    }

    private VerticalLayout createLeftLayout() {
        VerticalLayout left = new VerticalLayout(name);
        left.setPadding(false);
        left.setSpacing(false);
        left.setWidthFull();
        return left;
    }

    private VerticalLayout createCenterLayout() {
        VerticalLayout center = new VerticalLayout(progressBar);
        center.setPadding(false);
        center.setSpacing(false);
        progressBar.setWidth("100%");
        progressBar.getStyle()
                .set("background-color", "white")
                .set("border", "2px solid #ffffff")
                .set("border-radius", "10px");
        progressBar.setIndeterminate(false);
        progressBar.setHeight("10px");
        progressBar.addClassName("opaque-progress");
        return center;
    }

    private VerticalLayout createRightLayout() {
        VerticalLayout right = new VerticalLayout(mainLanguage, size);
        right.setPadding(false);
        right.setSpacing(true);
        right.setAlignItems(FlexComponent.Alignment.END);
        return right;
    }

    private void configureMainLayout(HorizontalLayout layout) {
        layout.setWidthFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        layout.setPadding(true);
        layout.setSpacing(true);
    }

    private void applyCardStyles() {
        addClassName("deque-ui");
        setWidthFull();
        getStyle()
                .set("padding", "1rem")
                .set("margin", "1rem");
    }

    private void applyTextColors() {
        name.getStyle().set("color", "white");
        size.getStyle().set("color", "white");
        mainLanguage.getStyle().set("color", "white");
    }

    public void setSizeText(String value) {
        size.setText("Size: " + value);
    }

    public void setMainLanguage(String lang) {
        mainLanguage.setText("Main: " + lang);
    }

    public void setProgress(double value) {
        progressBar.setValue(value*0.01);
    }
}
