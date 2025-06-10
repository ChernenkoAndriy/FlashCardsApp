package com.example.application.data;


import jakarta.persistence.*;

@Entity
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
