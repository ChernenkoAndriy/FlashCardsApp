package com.example.application.views.GameView;

import com.example.application.data.Card;
import com.example.application.service.CardService;
import com.example.application.views.Components.MainLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RolesAllowed({"ADMIN", "USER"})
@Route(value = "game", layout = MainLayout.class)
@PageTitle("Game | SLEEVE")
public class GameView extends VerticalLayout implements BeforeEnterObserver {
    private Queue<Task> tasks = new LinkedList<>();
    private GameCard gameCard = new GameCard();
    private BottomLayout layout;
    private HorizontalLayout bottom = new HorizontalLayout();
    private CardService cardService;
    private Task currentTask;
    private Integer deckId;
    private GameMode gameMode;
    private int score;
    private int currentTaskIndex;
    private int decksize;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public GameView(CardService service) {
        cardService = service;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        QueryParameters params = event.getLocation().getQueryParameters();
        Map<String, List<String>> paramMap = params.getParameters();

        List<String> deckIds = paramMap.get("deckId");
        List<String> gameModes = paramMap.get("mode");

        if (deckIds == null || deckIds.isEmpty()) {
            throw new RuntimeException("Missing required parameter: deckId");
        }

        if (gameModes == null || gameModes.isEmpty()) {
            throw new RuntimeException("Missing required parameter: mode");
        }

        try {
            deckId = Integer.parseInt(deckIds.get(0));
            gameMode = GameMode.valueOf(gameModes.get(0).toUpperCase());
            setSizeFull();
            setStyle();
            gameCard.setVisible(true);
            gameCard.addClickListener(clickevent -> {
                if(!gameCard.isFlipped()) {
                    gameCard.flipCard();
                    layout.flipVisibility();
                }
            });
            initialize(deckId, gameMode);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid format for deckId: must be an integer", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid mode value. Must be one of: " +
                    Arrays.toString(GameMode.values()), e);
        }
    }
    private void initialize(Integer deckId, GameMode gameMode) {
        List<Card> cards = cardService.findByDeckId(deckId);
        decksize = cards.size();
        for (Card card : cards) {
            tasks.add(new Task(card, gameMode));
        }
        currentTaskIndex = 0;
        nextTask();
    }
    private void setTask(Task task) {
        gameCard.setTask(task);
        if (task.getGameMode() == GameMode.SENTENCES) {
            layout = new SentenceLayout(v -> taskSuccess(), v -> taskFail());
        } else {
            layout = new ButtonLayout(v -> taskSuccess(), v -> taskFail());
        }
        layout.setSizeFull();
        bottom.add(layout);
    }
    private void nextTask() {
        if (tasks.isEmpty()) {
            gameFinished();
            return;
        }
        currentTask = tasks.poll();
        setTask(currentTask);
        gameCard.appearNormal();
    }
    private void taskFail() {
        bottom.removeAll();
        gameCard.flipCard();
        //tasks.add(currentTask);
        Task nextTask = tasks.poll();
        if (nextTask != null) {
            currentTask = nextTask;
            currentTaskIndex++;
            gameCard.transitionToNewTask(currentTask, false);
            scheduleBottomLayoutUpdate();
        } else {
            gameFinished();
        }
    }
    private void taskSuccess() {
        bottom.removeAll();
        gameCard.flipCard();
        if(currentTaskIndex<decksize) {
            score++;
        }
        Task nextTask = tasks.poll();
        if (nextTask != null) {
            currentTask = nextTask;
            currentTaskIndex++;
            gameCard.transitionToNewTask(currentTask, true);
            scheduleBottomLayoutUpdate();
        } else {
            gameFinished();
        }
    }
    private void scheduleBottomLayoutUpdate() {
        scheduler.schedule(() -> getUI().ifPresent(ui -> ui.access(() -> {
            setTask(currentTask);
        })), 600, TimeUnit.MILLISECONDS);
    }
    private void gameFinished() {
        GameFinishScreen finishedScreen = new GameFinishScreen(
                score,
                decksize,
                gameMode,
                () -> {
                    getUI().ifPresent(ui -> ui.navigate(""));
                }
        );
        removeAll();
        add(finishedScreen);
    }
    private void setStyle() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("overflow", "hidden");
        gameCard.setWidth("100%");
        gameCard.setHeight("100%");
        VerticalLayout cardLayout = new VerticalLayout(gameCard);
        cardLayout.setWidth("40%");
        cardLayout.setHeight("50%");
        cardLayout.setAlignItems(Alignment.CENTER);
        cardLayout.setJustifyContentMode(JustifyContentMode.START);
        cardLayout.setSpacing(true);
        cardLayout.setPadding(false);
        add(cardLayout);
        bottom.setWidth("40%");
        bottom.setHeight("15%");
        add(bottom);
    }
}
