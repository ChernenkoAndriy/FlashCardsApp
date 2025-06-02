package com.example.application.views.DeckEditor;

import com.example.application.data.Card;
import com.example.application.service.CardService;
import com.example.application.views.Components.MainLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RolesAllowed({"ADMIN", "USER"})
@Route(value = "editor", layout = MainLayout.class)
@PageTitle("DeckEditor | SLEEVE")
public class DeckEditorView extends VerticalLayout {
    private final DeckHeader header = new DeckHeader("New Deck");
    private final CardTable table = new CardTable();
    private  CreatorForm form;
    private CardService service;
    @Autowired
    public DeckEditorView(CardService service) {
        form = new CreatorForm(this::saveCard, this::deleteCard);
        this.service = service;
        configureStyle();
        configureComponents();
        addComponents();
        loadTestCards();
    }
    private void configureComponents() {
        table.addSelectionListener(event -> {
            form.setCard(event.getFirstSelectedItem().orElse(new Card()));
        });
    }
    void deleteCard(Card card){
        try {
            System.out.println("Card deleted: " + card.getWord());
           service.delete(card);
        }catch (Exception e){

        }
     }
     void saveCard(Card card){
        try {
             if (card.getId() == null) {
                 service.save(card);
                 System.out.println("Card saved: " + card.getWord());
             } else {
                 service.update(card);
                 System.out.println("Card updated: " + card.getWord());
             }
         }catch (Exception e){

        }
     }
    private void loadTestCards() {
        List<Card> fakeCards = new ArrayList<>();
        IntStream.rangeClosed(1, 1000).forEach(i -> {
            fakeCards.add(new Card(
                    "Word " + i,
                    "Translation " + i,
                    "Definition of the word number " + i + ". This is a sample long definition to check how it looks when the text is very wide or wraps around.",
                    "https://example.com/image" + i + ".png",
                    1L
            ));
        });

        table.setItems(fakeCards);
    }
    //Стилізація
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

}
