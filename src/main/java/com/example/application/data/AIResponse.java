package com.example.application.data;

public class AIResponse {
    private String cardWord;
    private String userSentence;
    private String aiFeedback;
    private boolean isCorrect;

    public AIResponse(String cardWord, String userSentence, String aiFeedback) {
        this.cardWord = cardWord;
        this.userSentence = userSentence;
        this.aiFeedback = aiFeedback;
        this.isCorrect = aiFeedback.toLowerCase().contains("the sentence is correct");
    }

    public String getCardWord() {
        return cardWord;
    }

    public String getUserSentence() {
        return userSentence;
    }

    public String getAiFeedback() {
        return aiFeedback;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}