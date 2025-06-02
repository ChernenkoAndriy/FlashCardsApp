package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "`card`")
public class Card {

    @Id
    @GeneratedValue
    @Column(name = "`id`", nullable = false)
    private Integer id;

    @Column(name = "`word`", nullable = false)
    private String word;

    @Column(name = "`translate`", nullable = false)
    private String translate;

    @Column(name = "`definition`", nullable = false)
    private String definition;

    @Column(name = "`image`", nullable = true)
    private String image;

    @Column(name = "`deck_id`", nullable = false)
    private Integer deckId;

    public Card(String word, String translate, String definition, String image, Integer deckId) {
        this.word = word;
        this.translate = translate;
        this.definition = definition;
        this.image = image;
        this.deckId = deckId;
    }

    public Card() {
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
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

    public Integer getDeckId() {
        return deckId;
    }

    public void setDeckId(Integer deckId) {
        this.deckId = deckId;
    }
}
