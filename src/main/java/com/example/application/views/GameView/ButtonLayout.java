package com.example.application.views.GameView;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.function.Consumer;


public class ButtonLayout extends BottomLayout {
    private H2 label = new H2("Have you guessed the word?");
    private Button success = new Button("Yes");
    private Button cancel = new Button("No");
    public ButtonLayout(Consumer<Void> onSuccess, Consumer<Void> onFail) {
        super(onSuccess, onFail);
        success.addClickListener(e -> onSuccess.accept(null));
        cancel.addClickListener(e -> onFail.accept(null));
        setHeightFull();
        label.getStyle()
                .set("width", "100%")
                .set("text-align", "center")
                .set("margin", "0")
                .set("height", "100%")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center");
        success.setWidth("33%");
        success.setHeightFull();
        cancel.setWidth("33%");
        cancel.setHeightFull();
        success.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        HorizontalLayout buttonLayout = new HorizontalLayout(success, cancel);
        buttonLayout.setWidthFull();
        buttonLayout.setHeightFull();
        buttonLayout.setPadding(false);
        buttonLayout.setSpacing(false);
        buttonLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        buttonLayout.setAlignItems(Alignment.STRETCH);
        add(label, buttonLayout);
        setVisible(false);
    }
}
