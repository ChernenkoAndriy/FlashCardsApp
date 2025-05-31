package com.example.application.data;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Language {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "`name`", nullable = false)
    private String name;

    public Language(String name) {
        this.name = name;
    }
    public Language() {
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
