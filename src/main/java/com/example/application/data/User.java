// User.java (оновлена версія)
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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private int workload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    public enum Role {
        USER, ADMIN
    }

    // Конструктори
    public User() {}

    public User(String username, String email, String password, String language, Integer workload) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.language = language;
        this.workload = workload;
        this.role = Role.USER;
    }

    public User(String username, String email, String password, String language, Integer workload, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.language = language;
        this.workload = workload;
        this.role = role;
    }

    // Геттери та сеттери
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public int getWorkload() { return workload; }
    public void setWorkload(int workload) { this.workload = workload; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
