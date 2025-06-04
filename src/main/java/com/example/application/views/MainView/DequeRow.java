package com.example.application.views.MainView;

import com.example.application.dto.DeckDto;
import com.example.application.views.MainView.Buttons.DeleteButton;
import com.example.application.views.MainView.Buttons.EditButton;
import com.example.application.views.MainView.Buttons.PlayButton;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.function.Consumer;

public class DequeRow extends HorizontalLayout {

    private DequeCard dequeCard;
    private Button deleteButton;
    private Button editButton;
    private Button playButton;

    /**
     * @param deckDto     The deck to display.
     * @param onDelete    Action to perform when delete is clicked.
     * @param onEdit      Action to perform when edit is clicked.
     * @param onPlay      Action to perform when play is clicked.
     */
    public DequeRow(DeckDto deckDto,
                    Consumer<DeckDto> onDelete,
                    Consumer<DeckDto> onEdit,
                    Consumer<DeckDto> onPlay) {
        this.dequeCard = new DequeCard(deckDto);
        initializeComponents(deckDto, onDelete, onEdit, onPlay);
        configureLayout();
        applyStyles();
    }

    private void initializeComponents(DeckDto deckDto,
                                      Consumer<DeckDto> onDelete,
                                      Consumer<DeckDto> onEdit,
                                      Consumer<DeckDto> onPlay) {

        deleteButton = new DeleteButton(deckDto, onDelete);
        editButton   = new EditButton(deckDto, onEdit);
        playButton   = new PlayButton(deckDto, onPlay);

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
