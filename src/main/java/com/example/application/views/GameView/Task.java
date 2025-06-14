package com.example.application.views.GameView;

import com.example.application.data.Card;

public class Task {
    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
    public GameMode getGameMode() {
        return gameMode;
    }
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
    private String generatedSentence;
    private Card card;
    private GameMode gameMode;
    public Task(Card card, GameMode gameMode) {
        this.card = card;
        this.gameMode = gameMode;
    }
    public Task(Card card, String generatedSentence){
        this.card = card;
        this.gameMode = GameMode.SENTENCES;
        this.generatedSentence = generatedSentence;
    }

    public String getSentence() {
        return generatedSentence;
    }
}
