package com.library.library_management.controller;

import com.library.library_management.model.Book;
import com.library.library_management.model.Student;
import com.library.library_management.model.User; // Assuming a User model
import com.library.library_management.service.BookService;
import com.library.library_management.service.BookRequestService; // NEW SERVICE
import com.library.library_management.service.UserService; // NEW SERVICE (for getting student)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // NEW IMPORT
import org.springframework.web.bind.annotation.PostMapping; // NEW IMPORT
import org.springframework.web.bind.annotation.RequestParam; // NEW IMPORT
import org.springframework.security.core.Authentication; // NEW IMPORT
import org.springframework.security.core.context.SecurityContextHolder; // NEW IMPORT

import java.security.Principal; // NEW IMPORT
import java.util.List;

@Controller
@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
public class BookController {

    private final BookService bookService;
    private final BookRequestService bookRequestService; // 1. Inject NEW Service
    private final UserService userService; // 2. Inject Service to get Student/User details

    // Use constructor injection instead of @Autowired on fields for better practice
    public BookController(BookService bookService, BookRequestService bookRequestService, UserService userService) {
        this.bookService = bookService;
        this.bookRequestService = bookRequestService;
        this.userService = userService;
    }

    // Show home page with all books (The shared catalog view)
    @GetMapping("/books")
    public String viewBookCatalog(Model model, @RequestParam(value = "requestSent", required = false) String requestSent,
                                  @RequestParam(value = "error", required = false) String error) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("book", new Book()); // Placeholder for form submission (less critical here)

        // Add attributes for success/error messages after request
        if (requestSent != null) {
            model.addAttribute("message", "Book request submitted successfully! Wait for admin approval.");
        }
        if (error != null) {
            model.addAttribute("error", "Failed to submit book request.");
        }

        return "books";
    }

    /**
     * NEW: Handles the POST request from a student to request a book.
     * Accessible only by STUDENT role (secured via SecurityConfig/PreAuthorize).
     */
    @PreAuthorize("hasRole('STUDENT')") // Ensure only students can submit requests
    @PostMapping("/books/request/{bookId}")
    public String requestBook(@PathVariable Long bookId, Principal principal) {

        // 1. Get the authenticated User's details (using Principal)
        String username = principal.getName();
        User user = userService.findByUsername(username);

        // 2. Assuming User model has a method/property to get the associated Student
        //    (You must implement the logic in UserService/User model to link User to Student)
        Student student = user.getStudent();
        Book book = bookService.getBookById(bookId);

        if (book != null && student != null) {
            try {
                bookRequestService.createRequest(book, student);
                return "redirect:/books?requestSent=true";
            } catch (Exception e) {
                // Log the error (not shown here)
                return "redirect:/books?error=true";
            }
        }

        return "redirect:/books?error=true";
    }

    // ⛔ ALL CRUD operations for books have been moved to AdminController
}