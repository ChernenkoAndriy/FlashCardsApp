package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "`card`")
public class Card {

    @Id
    @GeneratedValue
    @Column(name = "`id`", nullable = false)
    private Long id;

    @Column(name = "`word`", nullable = false)
    private String word;

    @Column(name = "`translate`", nullable = false)
    private String translate;

    @Column(name = "`definition`", nullable = false)
    private String definition;

    @Column(name = "`image`", nullable = true)
    private String image;

    @Column(name = "`deck_id`", nullable = false)
    private Long deckId;

    public Card(String word, String translate, String definition, String image, Long deckId) {
        this.word = word;
        this.translate = translate;
        this.definition = definition;
        this.image = image;
        this.deckId = deckId;
    }

    public Card() {
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }
}
