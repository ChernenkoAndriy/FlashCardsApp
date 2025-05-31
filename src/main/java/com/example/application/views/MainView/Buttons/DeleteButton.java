package com.example.application.views.MainView.Buttons;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class DeleteButton extends MiniButton {
    public DeleteButton() {
        super();
        Icon deleteIcon = new Icon(VaadinIcon.TRASH);
        this.setIcon(deleteIcon);
        getElement().setAttribute("title", "Delete");
        addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY_INLINE);
    }
}
