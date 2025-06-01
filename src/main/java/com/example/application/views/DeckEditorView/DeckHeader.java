package com.example.application.views.DeckEditorView;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class DeckHeader extends HorizontalLayout {

    private Span title;

    public DeckHeader(String deckName) {
        title = new Span("Deck: " + deckName);
        title.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "1.2em");
        title.getStyle().set("padding-left", "2rem");
        add(title);
        setAlignItems(Alignment.BASELINE);
        setPadding(false);
        setSpacing(true);
        setWidthFull();
    }

    public void setDeckName(String deckName) {
        title.setText("Deck: " + deckName);
    }
}
