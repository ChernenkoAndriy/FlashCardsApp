package com.example.application.views.DeckEditor;

import com.example.application.data.Card;
import com.example.application.dto.CardDto;
import com.example.application.service.CardService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
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

        addColumn(new ComponentRenderer<>(card -> {
            if (card.getImage() != null && card.getImage().length > 0) {
                Image image = new Image();
                StreamResource resource = new StreamResource("image",
                        () -> new ByteArrayInputStream(card.getImage()));
                image.setSrc(resource);
                image.setWidth("40px");
                image.setHeight("40px");
                return image;
            } else {
                return new Span("-");
            }
        })).setHeader("Image").setAutoWidth(true);

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
                                <div style="position: relative; width: 30px; height: 30px;">
                                        <svg viewBox="0 0 36 36" width="30" height="30" style="transform: rotate(-90deg);">
                                          <path
                                            d="M18 3
                                               a 15 15 0 0 1 0 30
                                               a 15 15 0 0 1 0 -30"
                                            fill="none"
                                            stroke="#eee"
                                            stroke-width="6"
                                          />
                                          <path
                                            d="M18 3
                                               a 15 15 0 0 1 0 30
                                               a 15 15 0 0 1 0 -30"
                                            fill="none"
                                            stroke="${item.color}"
                                            stroke-width="6"
                                            stroke-dasharray="${item.percent}, 100"
                                          />
                                        </svg>
                                      </div>
                        """)
                .withProperty("percent", card -> switch (card.getPeriod()) {
                    case "created" -> 0;
                    case "learning" -> 20;
                    case "first" -> 40;
                    case "second" -> 60;
                    case "third" -> card.getIsLearned() ? 100 : 80;
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