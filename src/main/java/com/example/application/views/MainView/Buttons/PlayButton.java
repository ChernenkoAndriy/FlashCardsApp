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

            Button revision = new Button("Revisions", e -> {
                onPlay.accept(deckDto, GameMode.REVISION);
                dialog.close();
            });

            Button definition = new Button("Definitions", e -> {
                onPlay.accept(deckDto, GameMode.DEFINITIONS);
                dialog.close();
            });
            Button sentence = new Button("Sentences", e -> {
                onPlay.accept(deckDto, GameMode.SENTENCES);
                dialog.close();
            });
            Button sentenceCreator = new Button("Sentence Creator", e -> {
                onPlay.accept(deckDto, GameMode.SENTENCECREATOR);
                dialog.close();
            });
            revision.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            definition.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            sentence.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            sentenceCreator.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


            revision.setWidthFull();
            definition.setWidthFull();
            sentence.setWidthFull();
            sentenceCreator.setWidthFull();

            revision.getStyle().set("font-size", "2em");
            definition.getStyle().set("font-size", "2em");
            sentence.getStyle().set("font-size", "2em");
            sentenceCreator.getStyle().set("font-size", "2em");


            buttonsLayout.add(revision, definition, sentence, sentenceCreator);
            buttonsLayout.expand(revision, definition, sentence, sentenceCreator);

            dialog.add(buttonsLayout);
            dialog.setWidth("500px");
            dialog.setHeight("500px");
            dialog.open();
        });
    }
}
