package com.example.application.views.MainView.Buttons;

import com.example.application.dto.DeckDto;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.function.Consumer;

//в кнопку видалення в рядку краще передати id деки для видалення і метод з MainView
// який за id видалить колоду і оновить сторінку, щоб колоди не було
public class DeleteButton extends MiniButton {
    public DeleteButton(DeckDto deckDto, Consumer<DeckDto> onDelete) {
        super();
        Icon deleteIcon = new Icon(VaadinIcon.TRASH);
        this.setIcon(deleteIcon);
        getElement().setAttribute("title", "Delete");
        addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY_INLINE);

        this.addClickListener(event -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Confirm Deletion");
            dialog.add("Are you sure you want to delete this deck?");

            Button confirm = new Button("Delete", e ->{
                onDelete.accept(deckDto);
                dialog.close();
            });
            Button cancel = new Button("Cancel", e -> dialog.close());

            confirm.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
            dialog.getFooter().add(cancel, confirm);

            dialog.open();
        });
    }
}
