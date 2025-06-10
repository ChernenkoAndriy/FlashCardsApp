package com.example.application.views.MainView.Buttons;

import com.example.application.dto.DeckDto;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.function.BiConsumer;

public class ArchiveButton extends MiniButton {

    private final Icon icon;

    public ArchiveButton(DeckDto deckDto, BiConsumer<DeckDto, Boolean> onArchive, boolean isActive) {
        super();

        icon = VaadinIcon.ARCHIVE.create();
        icon.getElement().getClassList().add("crossed-icon"); // спочатку закреслена
        updateIcon(isActive);

        setIcon(icon);

        getStyle().set("color", "white");
        getStyle().set("background-color", "var(--lumo-contrast-30pct)");
        getStyle().set("position", "relative");  // для ::after перекреслення

        getElement().setAttribute("title", "Archive");

        addClickListener(event -> {
            boolean newActive = !isActive;
            onArchive.accept(deckDto, newActive);
            updateIcon(newActive);
        });
    }

    private void updateIcon(boolean isActive) {
        if (isActive) {
            icon.getElement().getClassList().remove("crossed-icon"); // прибрати перекреслення
        } else {
            icon.getElement().getClassList().add("crossed-icon"); // додати перекреслення
        }
    }
}
