package com.library.library_management.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // Still needed if you want a formatted String getter

@Entity
public class IssuedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Student who borrowed
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false) // <--- ADDED: nullable = false
    private Student student;

    // Book that was borrowed
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false) // <--- ADDED: nullable = false
    private Book book;

    // --- ENHANCED LOAN TRACKING FIELDS ---

    // Store as LocalDate for comparison and calculation (best practice)
    @Column(nullable = false)
    private LocalDate issueDate;

    // Store as LocalDate for comparison and calculation (best practice)
    @Column(nullable = false)
    private LocalDate dueDate;

    // Field for future fine feature
    private Double fineAmount = 0.0;

    // Optional: Status (Issued / Returned)
    private String status = "Issued";

    // --- CONSTRUCTORS ---

    public IssuedBook() {
        // Default constructor for JPA
    }

    // Custom constructor for ease of creation, setting dates immediately
    public IssuedBook(Student student, Book book) {
        this.student = student;
        this.book = book;
        this.issueDate = LocalDate.now(); // Set issue date to today
        // Set due date to 15 days from now
        this.dueDate = LocalDate.now().plusDays(15);
        this.status = "Issued";
        this.fineAmount = 0.0;
    }

    // --- GETTERS & SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    // Use LocalDate for internal logic
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    // Use LocalDate for internal logic
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getFineAmount() { return fineAmount; }
    public void setFineAmount(Double fineAmount) { this.fineAmount = fineAmount; }

    // Optional: Helper method to return DueDate as a formatted String for the view (DD-MM-YYYY)
    public String getFormattedDueDate() {
        return this.dueDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    // Optional: Helper method to return IssueDate as a formatted String for the view
    public String getFormattedIssueDate() {
        return this.issueDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}