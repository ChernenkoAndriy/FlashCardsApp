package com.example.application.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "`user_progress`")
public class UserProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "`user_id`", nullable = false)
    private Integer userId;

    @Column(name = "`card_id`", nullable = false)
    private Integer cardId;

    @Column(name = "`period`", nullable = false)
    private String period = Period.created.name();

    @Column(name = "`is_correct`", nullable = false)
    private boolean isCorrect;

    @Column(name = "`is_learned`", nullable = false)
    private boolean isLearned;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    @Column(name = "`next_date`", nullable = true)
    private LocalDateTime nextDate;


    public UserProgress() {
    }

    public UserProgress(Integer userId, Integer cardId, String period, boolean isCorrect, boolean isLearned, LocalDateTime date, LocalDateTime nextDate) {
        this.userId = userId;
        this.cardId = cardId;
        this.period = period;
        this.isCorrect = isCorrect;
        this.isLearned = isLearned;
        this.date = date;
        this.nextDate = nextDate;
    }

    public UserProgress(Integer id, Integer userId, Integer cardId, String period, boolean isCorrect, boolean isLearned, LocalDateTime date, LocalDateTime nextDate) {
        this.id = id;
        this.userId = userId;
        this.cardId = cardId;
        this.period = period;
        this.isCorrect = isCorrect;
        this.isLearned = isLearned;
        this.date = date;
        this.nextDate = nextDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getNextDate() {
        return nextDate;
    }

    public void setNextDate(LocalDateTime nextDate) {
        this.nextDate = nextDate;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

