package com.example.application.views.MainView;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

import java.util.function.Consumer;

public class WorkLoadDialog extends Dialog {
    public WorkLoadDialog(Consumer<Integer> updateWorkLoad, int initialValue) {

        H3 h = new H3("Set your workload");
        VerticalLayout layout = new VerticalLayout();
        IntegerField numberField = new IntegerField("Enter your workload");
        numberField.setMin(0);
        numberField.setStepButtonsVisible(true);
        numberField.setValue(initialValue);
        Button button = new Button("OK", e -> {
            if (numberField.getValue() != null && numberField.getValue() > 0) {
                updateWorkLoad.accept(numberField.getValue());
                close();
            }
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layout.setWidth("100%");
        layout.add(h, numberField, button);
        h.setWidthFull();
        numberField.setWidthFull();
        button.setWidthFull();
        add(layout);
        open();
    }
}
