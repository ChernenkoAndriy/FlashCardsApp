package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "`card`")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", nullable = false)
    private Integer id;

    @Column(name = "`word`", nullable = false)
    private String word;

    @Column(name = "`translate`", nullable = false)
    private String translate;

    @Column(name = "`definition`", nullable = false)
    private String definition;
    @Lob
    @Column(name = "`image`", columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(name = "`image_content_type`", nullable = true)
    private String imageType;
    @Column(name = "`deck_id`", nullable = false)
    private Integer deckId;

    public Card(String word, String translate, String definition, byte[] image, Integer deckId) {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imagType) {
        this.imageType = imageType;
    }

    public Integer getDeckId() {
        return deckId;
    }

    public void setDeckId(Integer deckId) {
        this.deckId = deckId;
    }
}
