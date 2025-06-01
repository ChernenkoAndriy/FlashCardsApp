package com.example.application.views.MainView;

import com.example.application.views.MainView.Buttons.DeleteButton;
import com.example.application.views.MainView.Buttons.EditButton;
import com.example.application.views.MainView.Buttons.PlayButton;
import com.example.application.views.MainView.DequeCard;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class DequeRow extends HorizontalLayout {

    private DequeCard dequeCard;
    private Button deleteButton;
    private Button editButton;
    private Button playButton;
//тут можна зробити щоб в конструктор передалась сама дека,
// а картка автоматично створювалась від деки, щоб не створювати з ззовні
    //також потрібно передати лямбдою методи, які будуть виконувати кнопки

    public DequeRow(DequeCard dequeCard) {
        this.dequeCard = dequeCard;
        initializeComponents();
        configureLayout();
        applyStyles();
    }

    private void initializeComponents() {
        deleteButton = new DeleteButton();
        editButton   = new EditButton();
        playButton   = new PlayButton();
        dequeCard.setWidth("60%");
    }

    private void configureLayout() {
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        add(dequeCard);
        addToEnd(deleteButton, editButton, playButton);
    }

    private void applyStyles() {
        getStyle().set("padding", "0.5rem 0");
    }
}
