// EditButton.java
package com.example.application.views.MainView.Buttons;

import com.example.application.dto.DeckDto;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.function.Consumer;

public class EditButton extends MiniButton {
    public EditButton(DeckDto deckDto, Consumer<DeckDto> onEdit) {
        super();
        getStyle().set("background-color", "var(--lumo-secondary-color)");
        getStyle().set("color", "white");
        Icon editIcon = new Icon(VaadinIcon.EDIT);
        this.setIcon(editIcon);
        getElement().setAttribute("title", "Edit");
        addClickListener(event -> {onEdit.accept(deckDto);});
    }
}