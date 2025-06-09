package com.example.application.views.DeckEditor;

import com.example.application.data.Card;
import com.example.application.data.Deck;
import com.example.application.dto.CardDto;
import com.example.application.service.CardService;
import com.example.application.service.DeckService;
import com.example.application.views.Components.MainLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RolesAllowed({"ADMIN", "USER"})
@Route(value = "editor", layout = MainLayout.class)
@PageTitle("DeckEditor | SLEEVE")
public class DeckEditorView extends VerticalLayout implements BeforeEnterObserver {
    private final DeckHeader header = new DeckHeader("New Deck");
    private final CardTable table = new CardTable();
    private CreatorForm form;
    private CardService cardService;
    private final DeckService deckService;
    private Deck deck;

    @Autowired
    public DeckEditorView(CardService cardService, DeckService deckService) {
        this.cardService = cardService;
        this.deckService = deckService;

        form = new CreatorForm(card -> {
            card.setDeckId(deck.getId()); // Обов’язково!
            cardService.save(card);       // Зберігає сутність

            refreshTable();               // Оновлює DTO у таблиці
        }, card -> {
            cardService.delete(card);
            refreshTable();
        });

        configureStyle();
        configureComponents();
        addComponents();

        // Якщо хочеш початково завантажити тестові дані, розкоментуй цей рядок
        // loadTestCards();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Спробуємо отримати параметр deckId з URL
        QueryParameters params = event.getLocation().getQueryParameters();
        List<String> deckIds = params.getParameters().get("deckId");

        if (deckIds != null && !deckIds.isEmpty()) {
            try {
                Integer deckId = Integer.parseInt(deckIds.get(0));
                Deck deckDto = deckService.findById(deckId).orElse(null);
                if (deckDto != null) {
                    editDeck(deckDto);
                } else {
                    header.setDeckName("Deck not found");
                }
            } catch (NumberFormatException e) {
                header.setDeckName("Invalid deckId");
            }
        }
    }

    private void editDeck(Deck deckDto) {
        this.deck = deckDto;
        header.setDeckName(deckDto.getName());
        refreshTable();
    }

    private void refreshTable() {
        if (deck != null) {
            List<CardDto> cards = cardService.findCardDtosByUserAndDeckId(3, deck.getId()); //TODO реальний ID користувача
            table.setItems(cards);
        }
    }

    private void configureComponents() {
        table.addSelectionListener(event -> {
            event.getFirstSelectedItem().ifPresent(dto -> {
                cardService.findById(dto.getId()).ifPresent(originalCard -> {
                    Card copy = new Card();
                    copy.setId(originalCard.getId());
                    copy.setWord(originalCard.getWord());
                    copy.setTranslate(originalCard.getTranslate());
                    copy.setDefinition(originalCard.getDefinition());
                    copy.setImage(originalCard.getImage());
                    form.setCard(copy);
                });
            });
        });
    }

    void deleteCard(Card card) {
        try {
            cardService.delete(card);
            refreshTable();
        } catch (Exception e) {
            // Логіка обробки помилки
            e.printStackTrace();
        }
    }

    void saveCard(Card card) {
        if (deck == null) {
            // Якщо deck не вибраний, можна додати логіку або повідомлення користувачу
            return;
        }
        card.setDeckId(deck.getId());
        try {
            cardService.save(card);
            refreshTable();
        } catch (Exception e) {
            // Логіка обробки помилки
            e.printStackTrace();
        }
    }

    private void configureStyle() {
        getStyle().set("padding", "2rem 3rem 0rem 3rem");
        table.setWidth("100%");
        form.setWidth("34%");
    }

    private void addComponents() {
        setSizeFull();
        VerticalLayout tableWrapper = new VerticalLayout(table);
        tableWrapper.setWidth("66%");
        tableWrapper.setHeightFull();
        SplitLayout splitLayout = new SplitLayout(tableWrapper, form);
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(66);
        add(header, splitLayout);
    }

    // Для тестових даних - можна закоментувати чи видалити, коли будуть реальні дані
    /*private void loadTestCards() {
        List<Card> fakeCards = new java.util.ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            fakeCards.add(new Card(
                    "Word " + i,
                    "Translation " + i,
                    "Definition of the word number " + i + ". This is a sample long definition to check how it looks when the text is very wide or wraps around.",
                    "https://example.com/image" + i + ".png",
                    1
            ));
        }
        table.setItems(fakeCards);
    }*/
}
