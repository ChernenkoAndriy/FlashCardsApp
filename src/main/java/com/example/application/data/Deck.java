package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "`deck`")
public class Deck {


    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "`name`", nullable = false)
    private String name;

    @Column(name ="`cards_number`", nullable = false)
    private int cardsNumber;

    @Column(name = "`is_active`", nullable = false)
    private boolean isActive;

    @Column(name ="`language_id`", nullable = false)
    private Integer languageId;

    public Deck(String name, int cardsNumber, boolean isActive, Integer languageId) {
        this.name = name;
        this.cardsNumber = cardsNumber;
        this.isActive = isActive;
        this.languageId = languageId;
    }
    public Deck() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCardsNumber() {
        return cardsNumber;
    }

    public void setCardsNumber(int cardsNumber) {
        this.cardsNumber = cardsNumber;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
