package com.example.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Double amount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Expense() {}

    public Expense(String title, Double amount, LocalDate date, Category category, User user) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.user = user;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public Category getCategory() { return category; }
    public User getUser() { return user; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setCategory(Category category) { this.category = category; }
    public void setUser(User user) { this.user = user; }
}
