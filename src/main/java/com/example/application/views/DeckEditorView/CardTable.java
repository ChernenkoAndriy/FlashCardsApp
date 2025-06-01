package com.example.application.views.DeckEditorView;

import com.example.application.data.Card;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.LitRenderer;

import java.util.List;

public class CardTable extends Grid<Card> {

    public CardTable() {
        super(Card.class, false);
        configureColumns();
        addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void configureColumns() {
        addColumn(card -> getIndex(card) + 1)
                .setHeader("№");

        addColumn(Card::getWord)
                .setHeader("Word")
                .setWidth("30%")
                .setSortable(true);

        addColumn(Card::getTranslate)
                .setHeader("Translate")
                .setWidth("30%")
                .setSortable(true);

        addColumn(LitRenderer.<Card>of(
                        "<div style='white-space: normal;'>${item.definition}</div>")
                .withProperty("definition", Card::getDefinition))
                .setHeader("Definition")
                .setWidth("30%");
    }

    private int getIndex(Card card) {
        List<Card> items = getListDataView().getItems().toList();
        return items.indexOf(card);
    }
}
