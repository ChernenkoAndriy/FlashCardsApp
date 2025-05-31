// EditButton.java
package com.example.application.views.MainView.Buttons;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class EditButton extends MiniButton {
    public EditButton() {
        super();
        getStyle().set("background-color", "var(--lumo-secondary-color)");
        getStyle().set("color", "white");
        Icon editIcon = new Icon(VaadinIcon.EDIT);
        this.setIcon(editIcon);
        getElement().setAttribute("title", "Edit");
    }
}