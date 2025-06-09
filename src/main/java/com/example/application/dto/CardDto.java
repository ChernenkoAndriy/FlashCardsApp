package com.example.application.dto;

public class CardDto {
    private Integer id;
    private String word;
    private String translate;
    private String definition;
    private String image;
    private String period;

    // Конструктор
    public CardDto(Integer id, String word, String translate, String definition, String image, String period) {
        this.id = id;
        this.word = word;
        this.translate = translate;
        this.definition = definition;
        this.image = image;
        this.period = period;
    }

    public CardDto() {
    }

    public Integer getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public String getTranslate() {
        return translate;
    }

    public String getDefinition() {
        return definition;
    }

    public String getImage() {
        return image;
    }

    public String getPeriod() {
        return period;
    }


}