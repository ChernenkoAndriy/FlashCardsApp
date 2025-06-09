package com.example.application.views.DeckEditor;

import com.example.application.data.Card;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import java.util.function.Consumer;
public class CreatorForm extends VerticalLayout {
    private final TextField word = new TextField("Word");
    private final TextField translate = new TextField("Translation");
    private final TextArea image = new TextArea("Image URL");
    private final TextArea definition = new TextArea("Definition");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Binder<Card> binder = new BeanValidationBinder<>(Card.class);
    private Card card;
    public CreatorForm(Consumer<Card> onSave, Consumer<Card> onDelete) {
        configureBinder();
        configureLayout();
        configureFields();
        configureButtons();
        configureListeners(onSave, onDelete);
        binder.bindInstanceFields(this);
        Component buttonLayout = createButtonsLayout();
        add(word, translate, image, definition, buttonLayout);
    }
    private void configureBinder(){
        binder.forField(word)
                .asRequired("The world is required")
                .withValidator(new StringLengthValidator("Word must be between 1 and 50 characters", 1, 50))
                .bind(Card::getWord, Card::setWord);
        binder.forField(translate)
                .asRequired("The translation is required")
                .withValidator(new StringLengthValidator("Translation must be between 1 and 50 characters", 1, 50))
                .bind(Card::getTranslate, Card::setTranslate);
        binder.forField(definition)
                .asRequired()
                .withValidator(new StringLengthValidator("Definition must be between 1 and 2000 characters", 1, 2000))
                .bind(Card::getDefinition, Card::setDefinition);
        binder.forField(image)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator("Image URL must be between 1 and 2048 characters", 1, 2048))
                .bind(Card::getImage, Card::setImage);
    }
    public void setCard(Card card) {
        this.card = card;
        binder.readBean(card);
    }
    private void configureListeners(Consumer<Card> onSave, Consumer<Card> onDelete) {
        save.addClickListener(event -> {
            if (binder.isValid()) {
                if (card == null) {
                    card = new Card();
                }
                try {
                    Card target = card;
                    binder.writeBean(card);
                    System.out.println("Saving card with id: " + target.getId()); // 🔍 логування ID
                    onSave.accept(card);
                    setCard(new Card());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        delete.addClickListener(event -> {
            if (card != null) {
                onDelete.accept(card);
                setCard(new Card());
            }
        });
    }
    private void configureLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);
        setPadding(true);
    }
    private void configureFields() {
        for (TextField field : new TextField[]{word, translate}) {
            field.setWidthFull();
            field.setMaxWidth("400px");
        }
        image.setWidthFull();
        image.setMaxWidth("400px");
        definition.setWidthFull();
        definition.setMaxWidth("400px");
        definition.setHeight("150px");
    }
    private void configureButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
    }
    private Component createButtonsLayout() {
        HorizontalLayout buttons = new HorizontalLayout(save, delete);
        buttons.setMaxWidth("400px");
        buttons.setSpacing(true);
        buttons.setPadding(false);
        buttons.setAlignItems(Alignment.STRETCH);
        buttons.setJustifyContentMode(JustifyContentMode.CENTER);
        buttons.setFlexGrow(1.0, save, delete);
        save.setWidthFull();
        delete.setWidthFull();
        return buttons;
    }

}