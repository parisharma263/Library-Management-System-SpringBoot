package com.library.library_management.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;

    @Column(name = "issued_quantity", nullable = false)
    private int issuedQuantity = 0;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity = 1;

    // --- CASCADE DELETE FIX ---
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookRequest> requests;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssuedBook> issuedRecords;

    public Book() {
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getIssuedQuantity() {
        return issuedQuantity;
    }

    public void setIssuedQuantity(int issuedQuantity) {
        this.issuedQuantity = issuedQuantity;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    // --- FIX 1: ADD CALCULATED GETTER ---
    /**
     * Calculates the available quantity based on total and issued quantities.
     * @return The number of books currently available.
     */
    @Transient // Tells JPA not to map this to a column
    public int getAvailableQuantity() {
        return this.totalQuantity - this.issuedQuantity;
    }
}