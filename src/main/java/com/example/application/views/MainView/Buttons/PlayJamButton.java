package com.example.application.views.MainView.Buttons;


import com.example.application.data.Deck;
import com.example.application.data.Language;
import com.example.application.service.CardService;
import com.example.application.views.GameView.GameMode;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.function.BiConsumer;

public class PlayJamButton extends Button {
    private final Integer languageId;

    public PlayJamButton(Integer languageId, CardService cardService, Integer currentUserId, BiConsumer<Integer, GameMode> onPlayLanguage) {
        super();
        this.languageId = languageId;

        Icon playIcon = new Icon(VaadinIcon.PLAY);
        this.setIcon(playIcon);
        addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getElement().setAttribute("title", "Play all decks in language");

        this.addClickListener(event -> {
            if (cardService.getJam(currentUserId, languageId).isEmpty()) {
                Dialog dialog = new Dialog();
                dialog.setHeaderTitle("Sorry, you have no available cards of this language for today");

                VerticalLayout layout = new VerticalLayout();
                layout.setPadding(true);
                layout.setSpacing(true);
                layout.setAlignItems(VerticalLayout.Alignment.CENTER);

                Button createBtn = new Button("OK", clickEvent -> {
                    dialog.close();
                });

                createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

                layout.add(createBtn);

                dialog.add(layout);
                dialog.setWidth("500px");
                dialog.setHeight("200px");
                dialog.open();
            } else {
                onPlayLanguage.accept(languageId, GameMode.JAM);
            }
        });
    }
}

