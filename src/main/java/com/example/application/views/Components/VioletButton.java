package com.example.application.views.Components;

import com.vaadin.flow.component.button.Button;
//просто кнопочка але фіолетова
public class VioletButton extends Button {
    public VioletButton() {
        super();
        getStyle().set("background-color", "var(--lumo-secondary-color)");
        getStyle().set("color", "white");
    }
    public VioletButton(String text) {
        super(text);
        getStyle().set("background-color", "var(--lumo-secondary-color)");
        getStyle().set("color", "white");
    }
}