package com.example.application.views.MainView.Buttons;

import com.example.application.dto.DeckDto;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.function.Consumer;

public class ArchiveButton extends MiniButton {

    public ArchiveButton(DeckDto deckDto, Consumer<DeckDto> onArchive) {
        super();
        setIcon(VaadinIcon.ARCHIVE.create());
        getStyle().set("color", "white");
        getStyle().set("background-color", "var(--lumo-contrast-35pct)");

        getElement().setAttribute("title", "Archive");
        addClickListener(event -> onArchive.accept(deckDto));
    }
}