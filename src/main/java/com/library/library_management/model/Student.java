package com.library.library_management.model;

import jakarta.persistence.*;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String rollNumber;

    // Naya code yahan add hua hai
    // This is the correct side for the one-to-one mapping
    @OneToOne(mappedBy = "student")
    private User user;

    // --- CASCADE DELETE FIX ---
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<BookRequest> requests;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<IssuedBook> issuedRecords;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    // Naye field ke Getters and Setters (CONFIRM these are present!)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}