package com.example.application.dto;

public class DeckDto {
    private Integer id;
    private String name;
    private int cardsNumber;
    private String languageName;
    private Integer progress;


    /* цей клас потрібен для чистоти коду. конкретно його я створила, бо мені в DequeCard, яка створюється на Deck треба назву мови,
    а підключати туди LanguageService - недоречно
     */
    public DeckDto(Integer id, String name, int cardsNumber, String languageName, Integer progress) {
        this.id = id;
        this.name = name;
        this.cardsNumber = cardsNumber;
        this.languageName = languageName;
        this.progress = progress;
    }

    public Integer getId() {
        return id;
    }

    public String getLanguageName() {
        return languageName;
    }

    public int getCardsNumber() {
        return cardsNumber;
    }

    public String getName() {
        return name;
    }

    public Integer getProgress() {
        return progress;
    }


}