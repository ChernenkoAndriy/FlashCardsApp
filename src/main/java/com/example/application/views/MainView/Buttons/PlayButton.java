package com.example.application.views.MainView.Buttons;

import com.example.application.dto.DeckDto;
import com.example.application.views.GameView.GameMode;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

//отут як і в редагуванні треба якось передати id як конфігурацію на наступну сторінку
//але я хз як це зробити тож поки не чіпай, рівно як і кнопку edit
public class PlayButton extends MiniButton {
    public PlayButton(DeckDto deckDto, BiConsumer<DeckDto, GameMode> onPlay) {
        super();
        Icon playIcon = new Icon(VaadinIcon.PLAY);
        this.setIcon(playIcon);
        addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getElement().setAttribute("title", "Play");

        this.addClickListener(event -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Choose Mode");

            VerticalLayout buttonsLayout = new VerticalLayout();
            buttonsLayout.setSpacing(true);
            buttonsLayout.setPadding(true);
            buttonsLayout.setWidth("100%");
            buttonsLayout.setHeight("100%");

            Button revision = new Button("Revision", e -> {
                onPlay.accept(deckDto, GameMode.REVISION);
                dialog.close();
            });

            Button definition = new Button("Definition", e -> {
                onPlay.accept(deckDto, GameMode.DEFINITIONS);
                dialog.close();
            });

            Button advanced = new Button("Advanced", e -> {
                onPlay.accept(deckDto, GameMode.SENTENCES);
                dialog.close();
            });
            revision.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            definition.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            advanced.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            revision.setWidthFull();
            definition.setWidthFull();
            advanced.setWidthFull();

            // Задаємо вдвічі більший шрифт
            revision.getStyle().set("font-size", "2em");
            definition.getStyle().set("font-size", "2em");
            advanced.getStyle().set("font-size", "2em");

            buttonsLayout.add(revision, definition, advanced);
            buttonsLayout.expand(revision, definition, advanced);

            dialog.add(buttonsLayout);
            dialog.setWidth("500px");
            dialog.setHeight("500px");
            dialog.open();
        });
    }
}
