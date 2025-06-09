package com.example.application.views.DeckEditor;

import com.example.application.data.Card;
import com.example.application.dto.CardDto;
import com.example.application.service.CardService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.LitRenderer;

import java.util.List;

public class CardTable extends Grid<CardDto> {

    public CardTable() {
        super(CardDto.class, false);
        configureColumns();
        addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void configureColumns() {
        addColumn(card -> getIndex(card) + 1)
                .setHeader("№")
                .setWidth("5%");

        addColumn(CardDto::getWord)
                .setHeader("Word")
                .setWidth("20%")
                .setSortable(true);

        addColumn(CardDto::getTranslate)
                .setHeader("Translate")
                .setWidth("20%")
                .setSortable(true);

        addColumn(LitRenderer.<CardDto>of(
                        "<div style='white-space: normal;'>${item.definition}</div>")
                .withProperty("definition", CardDto::getDefinition))
                .setHeader("Definition")
                .setWidth("30%");

        addColumn(CardDto::getPeriod)
                .setHeader("Period")
                .setWidth("10%")
                .setSortable(true);
    }

    private int getIndex(CardDto card) {
        List<CardDto> items = getListDataView().getItems().toList();
        return items.indexOf(card);
    }
}
