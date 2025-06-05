package com.example.application.data;

import jakarta.persistence.*;

@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private int workload;


    public User() {
    }

    public User(String username, String email, String password, String language, Integer workload) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.language = language;
        this.workload = workload;
    }


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }

    public int getWorkload() {
        return workload;
    }
    public void setWorkload(int workload) {
        this.workload = workload;
    }

}
