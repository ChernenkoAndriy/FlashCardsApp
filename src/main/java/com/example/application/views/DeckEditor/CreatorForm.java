package com.example.application.views.DeckEditor;

import com.example.application.data.Card;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class CreatorForm extends VerticalLayout {
    private final TextField word = new TextField("Word");
    private final TextField translate = new TextField("Translation");
    private final TextArea definition = new TextArea("Definition");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button deletePicture = new Button("Delete picture");
    private final Binder<Card> binder = new BeanValidationBinder<>(Card.class);
    private Card card;
    private Upload upload;
    private final MemoryBuffer buffer = new MemoryBuffer();

    public CreatorForm(Consumer<Card> onSave, Consumer<Card> onDelete) {
        configureBinder();
        configureLayout();
        configureFields();
        initAndConfigureUpload();
        configureButtons();
        configureListeners(onSave, onDelete);
        binder.bindInstanceFields(this);
        Component buttonLayout = createButtonsLayout();
        add(word, translate, upload, definition, buttonLayout);
    }

    private void initAndConfigureUpload() {
        upload = new Upload();
        upload.setReceiver(buffer);
        upload.setAcceptedFileTypes("image/*");
        upload.setMaxFileSize(2 * 1024 * 1024); // 2MB

        upload.addFileRejectedListener(event -> {
            Notification.show("File too large. Max size is 2MB", 3000, Notification.Position.MIDDLE);
        });
        upload.setDropAllowed(true);
        upload.setMaxFiles(1);
        upload.setUploadButton(new Button("Upload Image"));
        upload.setDropLabel(new Span("Drag & drop image here"));

        upload.addSucceededListener(event -> {
            try {
                InputStream inputStream = buffer.getInputStream();
                byte[] imageBytes = inputStream.readAllBytes();

                if (card == null) {
                    card = new Card();
                }
                card.setImage(imageBytes);
                card.setImageType(event.getMIMEType());

                Notification.show("Image uploaded successfully", 3000, Notification.Position.MIDDLE);
            } catch (IOException e) {
                Notification.show("Error uploading image", 3000, Notification.Position.MIDDLE);
                e.printStackTrace();
            }
        });
    }

    private void configureBinder() {
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
    }

    public void setCard(Card card) {
        this.card = card;
        binder.readBean(card);
        resetUploadComponent();
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
                    System.out.println("Saving card with id: " + target.getId());
                    onSave.accept(card);
                    setCard(new Card());
                    resetUploadComponent();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        delete.addClickListener(event -> {
            if (card != null) {
                onDelete.accept(card);
                resetUploadComponent();
                setCard(new Card());
            }
        });
        deletePicture.addClickListener(event -> {
            if (card != null) {
                card.setImage(null);
                card.setImageType(null);
                if (binder.isValid()) {
                    if (card == null) {
                        card = new Card();
                    }
                    try {
                        Card target = card;
                        binder.writeBean(card);
                        System.out.println("Saving card with id: " + target.getId());
                        onSave.accept(card);
                        setCard(new Card());
                        resetUploadComponent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void resetUploadComponent() {
        Upload oldUpload = this.upload;


        initAndConfigureUpload();
        replace(oldUpload, this.upload);
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
        definition.setWidthFull();
        definition.setMaxWidth("400px");
        definition.setHeight("150px");
    }

    private void configureButtons() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        save.addClickShortcut(Key.ENTER);
        delete.addClickShortcut(Key.DELETE);

    }

    private Component createButtonsLayout() {
        VerticalLayout buttons = new VerticalLayout(save, delete, deletePicture);
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