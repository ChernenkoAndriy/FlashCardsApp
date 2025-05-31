package com.example.application.views.MainView.Buttons;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;

public class MiniButton extends Button {

    protected Icon icon;

    MiniButton() {
        setWidth("65px");
        setHeight("65px");
        getStyle().set("min-width", "65px")
                .set("min-height", "65px")
                .set("padding", "0")
                .set("margin-left", "4rem")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center");
    }


    public void setIcon(Icon icon) {
        this.icon = icon;
        if (icon != null) {
            icon.setSize("30px");
        }
        super.setIcon(icon);
    }
}
