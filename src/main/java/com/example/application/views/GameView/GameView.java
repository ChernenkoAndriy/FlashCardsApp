package com.example.application.views.GameView;

import com.example.application.data.Card;
import com.example.application.data.User;
import com.example.application.data.UserProgress;
import com.example.application.security.SecurityService;
import com.example.application.service.AIService;
import com.example.application.service.CardService;
import com.example.application.service.UserProgressService;
import com.example.application.service.UserService;
import com.example.application.views.Components.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RolesAllowed({"ADMIN", "USER"})
@Route(value = "game", layout = MainLayout.class)
@PageTitle("Game | SLEEVE")
public class GameView extends VerticalLayout implements BeforeEnterObserver {
    private final UserProgressService userProgressService;
    private Queue<Task> tasks = new LinkedList<>();
    private GameCard gameCard = new GameCard();
    private final HorizontalLayout bottom = new HorizontalLayout();
    private CardService cardService;
    private Task currentTask;
    private Integer deckId;
    private GameMode gameMode;
    private int score;
    private int currentTaskIndex;
    private int decksize;
    private AIService aiService;
    private ButtonLayout currentButtonLayout;
    private SentenceLayout currentSentenceLayout;
    private final SecurityService securityService;
    private final UserService userService;
    @Autowired
    public GameView(CardService service, AIService aiService, SecurityService securityService, UserService userService, UserProgressService userProgressService) {
        cardService = service;
        this.aiService = aiService;
        this.securityService = securityService;
        this.userService = userService;
        this.userProgressService = userProgressService;
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
            initialize(deckId, gameMode);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid format for deckId: must be an integer", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid mode value. Must be one of: " +
                    Arrays.toString(GameMode.values()), e);
        }
    }
    private void initialize(Integer deckId, GameMode gameMode) {
        if (gameMode == GameMode.JAM) {
            Integer currentUserId = securityService.getAuthenticatedUser()
                    .map(userDetails -> {
                        User user = userService.findByUsername(userDetails.getUsername());
                        return user != null ? user.getId() : null;
                    })
                    .orElse(null);

            List<Card> cards = cardService.getJam(currentUserId, deckId);
            decksize = cards.size();
            for (Card card : cards) {
                String currentPeriod = userProgressService.findByUserIdAndCardId(currentUserId, card.getId()).getPeriod();
                GameMode currentGameMode = switch (currentPeriod) {
                    case "created", "learning" -> GameMode.REVISION;
                    case "first" -> GameMode.DEFINITIONS;
                    case "second" -> GameMode.SENTENCES;
                    case "third" -> GameMode.SENTENCECREATOR;
                    default -> throw new IllegalStateException("Unexpected value: " + currentPeriod);
                };
                if (currentGameMode == GameMode.SENTENCES) {
                    tasks.add(new Task(card, aiService.createSentence(card)));
                } else {
                    tasks.add(new Task(card, currentGameMode));
                }
            }
        } else{
            List<Card> cards = cardService.findByDeckId(deckId);
            decksize = cards.size();
            for (Card card : cards) {
                if (gameMode == GameMode.SENTENCES) {
                    tasks.add(new Task(card, aiService.createSentence(card)));
                } else {
                    tasks.add(new Task(card, gameMode));
                }
            }
        }



        currentTaskIndex = 0;
        currentTask = tasks.poll();
        gameCard.addClickListener(clickevent -> {
            gameCard.flipCard();
            handleCardFlip();
        });
        setTask(currentTask);
        gameCard.appearNormal();
    }
    private void setTask(Task task) {
        if(task.getGameMode()==GameMode.SENTENCES){
            gameCard.setTask(task, task.getSentence());
        }else {
            gameCard.setTask(task);
        }
        if (task.getGameMode() == GameMode.SENTENCECREATOR) {
            addSentenceLayout(task);
        } else {
            addButtonLayout();
        }
    }
    private void validateInventedSentence(String answer, Card word) {
        String chatAnswer = aiService.checkSentenceWithCardWord(word, answer);
        if (chatAnswer.contains("The sentence is correct")) {
            taskSuccess();
        } else {

            Notification notification = new Notification();
            notification.setPosition(Notification.Position.MIDDLE);
            notification.setDuration(0);

            Span text = new Span(chatAnswer);
            Button closeButton = new Button("Got it!", e -> notification.close());
            closeButton.getElement().getStyle().set("margin-left", "auto");

            HorizontalLayout layout = new HorizontalLayout(text, closeButton);
            layout.setAlignItems(FlexComponent.Alignment.CENTER);
            layout.setWidthFull();
            //layout.getStyle().set("font-size", "1.2em"); // більший шрифт
            //layout.getStyle().set("padding", "1em");     // більший відступ

            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.add(layout);
            notification.open();


            taskFail();
        }
    }
    private void taskFail() {
        bottom.removeAll();
        gameCard.flipCard();

        cardService.markLearning(currentTask.getCard(), securityService.getAuthenticatedUser()
                .map(userDetails -> {
                    User user = userService.findByUsername(userDetails.getUsername());
                    return user != null ? user.getId() : null;
                })
                .orElse(null));
        Task nextTask = tasks.poll();
        if (nextTask != null) {
            currentTask = nextTask;
            currentTaskIndex++;
            gameCard.transitionToNewTask(currentTask, false, () -> {
                setTask(currentTask);
                bottom.setVisible(true);
            });
        } else {
            gameFinished();
        }
    }
    private void taskSuccess() {
        bottom.removeAll();
        gameCard.flipCard();
        if (currentTaskIndex < decksize) {
            score++;
        }
        cardService.markGuessed(currentTask.getGameMode(), currentTask.getCard(), securityService.getAuthenticatedUser()
                .map(userDetails -> {
                    User user = userService.findByUsername(userDetails.getUsername());
                    return user != null ? user.getId() : null;
                })
                .orElse(null));
        Task nextTask = tasks.poll();
        if (nextTask != null) {
            currentTask = nextTask;
            currentTaskIndex++;
            gameCard.transitionToNewTask(currentTask, true, () -> {
                setTask(currentTask);
                bottom.setVisible(true);
            });
        } else {
            gameFinished();
        }
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
    private void handleCardFlip() {
        if (currentTask.getGameMode() == GameMode.SENTENCECREATOR) {

        } else {
            if (currentButtonLayout != null) {
                currentButtonLayout.flipVisibility();
            }
        }
    }
    private void addButtonLayout() {
        bottom.removeAll();
        bottom.setVisible(false);
        currentButtonLayout = new ButtonLayout(v -> taskSuccess(), v -> taskFail());
        currentSentenceLayout = null;
        bottom.add(currentButtonLayout);
        bottom.setVisible(true);
        bottom.add(currentButtonLayout);
    }
    private void addSentenceLayout(Task task) {
        bottom.removeAll();
        bottom.setVisible(false);
        currentSentenceLayout = new SentenceLayout();
        currentButtonLayout = null;
        currentSentenceLayout.getSubmit().addClickListener(clickevent -> {
            validateInventedSentence(currentSentenceLayout.getAnswer(), task.getCard());
        });
        bottom.add(currentSentenceLayout);
        bottom.setVisible(true);
    }
}
