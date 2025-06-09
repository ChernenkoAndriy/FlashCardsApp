package com.example.application.views.GameView;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GameFinishScreen extends Div {

    public GameFinishScreen(int score, int totalCards, GameMode gameMode, Runnable onBackToMenu) {
        setSizeFull();

        // Layout
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setJustifyContentMode(VerticalLayout.JustifyContentMode.CENTER);
        mainLayout.setPadding(true);
        mainLayout.setSpacing(true);

        // Title
        H1 title = new H1("Game Completed");
        title.getStyle()
                .set("font-size", "3rem")
                .set("font-weight", "bold")
                .set("margin", "0")
                .set("color", "black");

        // Subtitle
        H2 performance = new H2(getPerformanceLevel(score, totalCards));
        performance.getStyle()
                .set("color", "#666")
                .set("margin-top", "0.5rem");

        // Stats
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setSpacing(true);
        statsLayout.setAlignItems(Alignment.CENTER);

        statsLayout.add(
                createStatCard("Score", String.valueOf(score), VaadinIcon.TROPHY),
                createStatCard("Total", String.valueOf(totalCards), VaadinIcon.RECORDS),
                createStatCard("Accuracy", String.format("%.1f%%", totalCards > 0 ? (double) score / totalCards * 100 : 0), VaadinIcon.CHECK_CIRCLE),
                createStatCard("Missed", String.valueOf(totalCards - score), VaadinIcon.CLOSE_CIRCLE)
        );

        // Game mode info
        Span mode = new Span("Mode: " + gameMode);
        mode.getStyle().set("margin-top", "1rem").set("font-weight", "500").set("text-transform", "uppercase");

        // Only one button: Back to Menu
        Button backBtn = new Button("Back to Menu", e -> onBackToMenu.run());
        backBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        mainLayout.add(title, performance, statsLayout, mode, backBtn);
        add(mainLayout);
    }

    private VerticalLayout createStatCard(String label, String value, VaadinIcon icon) {
        VerticalLayout card = new VerticalLayout();
        card.setAlignItems(Alignment.CENTER);
        card.setSpacing(false);
        card.setPadding(false);
        card.getStyle()
                .set("border", "2px solid black")
                .set("padding", "1rem")
                .set("min-width", "120px")
                .set("background", "#f0f0f0");

        Icon iconComponent = new Icon(icon);
        iconComponent.setSize("24px");

        Span valueSpan = new Span(value);
        valueSpan.getStyle()
                .set("font-size", "1.5rem")
                .set("font-weight", "bold");

        Span labelSpan = new Span(label);
        labelSpan.getStyle()
                .set("font-size", "0.9rem")
                .set("text-transform", "uppercase")
                .set("opacity", "0.7");

        card.add(iconComponent, valueSpan, labelSpan);
        return card;
    }

    private String getPerformanceLevel(int score, int total) {
        double accuracy = total > 0 ? (double) score / total * 100 : 0;
        if (accuracy >= 90) return "Excellent!";
        else if (accuracy >= 75) return "Great Job!";
        else if (accuracy >= 60) return "Good Work!";
        else if (accuracy >= 40) return "Keep Trying!";
        else return "Practice More!";
    }
}
