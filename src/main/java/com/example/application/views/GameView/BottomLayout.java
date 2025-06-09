package com.example.application.views.GameView;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.function.Consumer;

public class BottomLayout extends VerticalLayout {
    public void flipVisibility() {
        setVisible(!isVisible());
    }

}
