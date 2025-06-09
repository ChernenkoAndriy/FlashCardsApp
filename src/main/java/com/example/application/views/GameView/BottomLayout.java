package com.example.application.views.GameView;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.function.Consumer;

public class BottomLayout extends VerticalLayout {
    protected Consumer<Void> onSuccess;
    protected Consumer<Void> onFail;
    BottomLayout(Consumer<Void> onSuccess, Consumer<Void> onFail) {
        this.onSuccess = onSuccess;
        this.onFail = onFail;
    }
    public void flipVisibility() {
        setVisible(!isVisible());
    }

}
