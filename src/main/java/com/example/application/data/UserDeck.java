package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "`user_deck`")
public class UserDeck {


    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "`user_id`", nullable = false)
    private Integer userId;

    @Column(name = "`deck_id`", nullable = false)
    private Integer deckId;

    @Column(name = "`learned_number`", nullable = false)
    private Integer learnedNumber;

    @Column(name = "`progress`", nullable = false)
    private Integer progress;

    @Column(name = "`is_active`", nullable = false)
    private boolean isActive;


    public UserDeck(Integer userId, Integer deckId, Integer learnedNumber, Integer progress, boolean isActive) {
        this.userId = userId;
        this.deckId = deckId;
        this.learnedNumber = learnedNumber;
        this.progress = progress;
        this.isActive = isActive;
    }

    public UserDeck() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDeckId() {
        return deckId;
    }

    public void setDeckId(Integer deckId) {
        this.deckId = deckId;
    }

    public Integer getLearnedNumber() {
        return learnedNumber;
    }

    public void setLearnedNumber(Integer learnedNumber) {
        this.learnedNumber = learnedNumber;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}