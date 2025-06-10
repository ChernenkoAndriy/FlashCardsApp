package com.example.application.views.DeckEditor;

import com.example.application.data.Card;
import com.example.application.dto.CardDto;
import com.example.application.service.CardService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
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

        addColumn(LitRenderer.<CardDto>of(
                        """
                        <div style="position: relative; width: 40px; height: 40px;">
                          <svg viewBox="0 0 36 36" style="transform: rotate(-90deg);">
                            <path
                              d="M18 2.0845
                                 a 15.9155 15.9155 0 0 1 0 31.831
                                 a 15.9155 15.9155 0 0 1 0 -31.831"
                              fill="none"
                              stroke="#eee"
                              stroke-width="3.8"/>
                            <path
                              d="M18 2.0845
                                 a 15.9155 15.9155 0 0 1 0 31.831
                                 a 15.9155 15.9155 0 0 1 0 -31.831"
                              fill="none"
                              stroke="${item.color}"
                              stroke-width="3.8"
                              stroke-dasharray="${item.percent}, 100"
                              stroke-linecap="round"/>
                          </svg>
                        </div>
                        """)
                .withProperty("percent", card -> switch (card.getPeriod()) {
                    case "created" -> 0;
                    case "learning" -> 25;
                    case "first" -> 50;
                    case "second" -> 75;
                    case "third" -> card.getIsLearned() ? 100 : 90;
                    default -> 100;
                })
                .withProperty("color", card -> switch (card.getPeriod()) {
                    case "created" -> "#aaa";
                    case "learning" -> "#f90";
                    case "first" -> "#0cf";
                    case "second" -> "#0c0";
                    case "third" -> card.getIsLearned() ? "#060" : "#0f0";
                    default -> "#0f0";
                }))
                .setHeader("Progress")
                .setAutoWidth(true);
    }

    private int getIndex(CardDto card) {
        List<CardDto> items = getListDataView().getItems().toList();
        return items.indexOf(card);
    }
}