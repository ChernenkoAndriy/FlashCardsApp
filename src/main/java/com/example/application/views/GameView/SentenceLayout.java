package com.example.application.views.GameView;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.function.Consumer;

public class SentenceLayout extends BottomLayout{
    private TextArea answer= new TextArea();

    public Button getSubmit() {
        return submit;
    }

    private Button submit = new Button("Submit");

    SentenceLayout(){
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        answer.setWidthFull();
        answer.setPlaceholder("Create the sentence with this word");
        submit.setWidth("50%");
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(answer, submit);
        setVisible(true);
    }
    public String getAnswer() {
        return answer.getValue().trim();
    }
}
