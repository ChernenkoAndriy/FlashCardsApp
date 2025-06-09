package com.example.application.views.GameView;

import com.vaadin.flow.component.textfield.TextField;

import java.util.function.Consumer;

public class SentenceLayout extends BottomLayout{
    private TextField answer= new TextField("Enter your answer");
    SentenceLayout(Consumer<Void> onSuccess, Consumer<Void> onFail){
        super(onSuccess, onFail);
        setWidthFull();
        add(answer);
        setVisible(true);
    }
}
