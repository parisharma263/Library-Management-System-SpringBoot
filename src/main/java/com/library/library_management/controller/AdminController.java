package com.library.library_management.controller;

import com.library.library_management.model.Book;
import com.library.library_management.model.IssuedBook;
import com.library.library_management.model.Student;
import com.library.library_management.model.BookRequest; // NEW IMPORT
import com.library.library_management.service.BookService;
import com.library.library_management.service.IssuedBookService;
import com.library.library_management.service.StudentService;
import com.library.library_management.service.BookRequestService; // NEW IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final BookService bookService;
    private final StudentService studentService;
    private final IssuedBookService issuedBookService;
    private final BookRequestService bookRequestService; // 1. NEW SERVICE INJECTION

    // Use constructor injection for all services
    @Autowired
    public AdminController(BookService bookService, StudentService studentService,
                           IssuedBookService issuedBookService, BookRequestService bookRequestService) {
        this.bookService = bookService;
        this.studentService = studentService;
        this.issuedBookService = issuedBookService;
        this.bookRequestService = bookRequestService; // Initialize new service
    }

    // 1. ADMIN DASHBOARD
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    // --------------------------------------------------------------------------
    // 2. BOOK MANAGEMENT (CRUD - UNCHANGED)
    // --------------------------------------------------------------------------

    @PostMapping("/books/add")
    public String addBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/books/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "edit";
    }

    @PostMapping("/books/update")
    public String updateBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    // --------------------------------------------------------------------------
    // 3. STUDENT MANAGEMENT (CRUD - UNCHANGED)
    // --------------------------------------------------------------------------

    @GetMapping("/students")
    public String studentPage(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("student", new Student());
        return "students";
    }

    @PostMapping("/students/add")
    public String addStudent(@ModelAttribute Student student) {
        studentService.saveStudent(student);
        return "redirect:/admin/students";
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "edit_student";
    }

    @PostMapping("/students/update/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {
        student.setId(id);
        studentService.saveStudent(student);
        return "redirect:/admin/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/admin/students";
    }

    // --------------------------------------------------------------------------
    // 4. BOOK REQUEST MANAGEMENT (NEW FEATURE)
    // --------------------------------------------------------------------------

    // Show all pending book requests (maps to admin-requests.html)
    @GetMapping("/requests")
    public String viewPendingRequests(Model model) {
        List<BookRequest> pendingRequests = bookRequestService.findAllPendingRequests();
        model.addAttribute("requests", pendingRequests);
        return "admin-requests";
    }

    // Handle approval of a request and convert it to an IssuedBook
    @PostMapping("/requests/approve/{id}")
    public String approveRequest(@PathVariable Long id) {
        boolean success = bookRequestService.approveRequest(id);
        if (success) {
            return "redirect:/admin/requests?success=approved";
        }
        return "redirect:/admin/requests?error=issueFailed";
    }

    // Handle rejection of a request
    @PostMapping("/requests/reject/{id}")
    public String rejectRequest(@PathVariable Long id) {
        boolean success = bookRequestService.rejectRequest(id);
        if (success) {
            return "redirect:/admin/requests?success=rejected";
        }
        return "redirect:/admin/requests?error=rejectFailed";
    }

    // --------------------------------------------------------------------------
    // 5. ISSUED BOOK MANAGEMENT (LOAN TRACKING)
    // --------------------------------------------------------------------------

    // **REMOVED:** showIssueForm() endpoint has been removed as issuance is now via /admin/requests.
    // **REMOVED:** issueBook() endpoint has been removed as issuance is now via /admin/requests.

    // Show all issued books (The comprehensive management list - UNCHANGED)
    @GetMapping("/issued")
    public String viewIssuedBooks(Model model) {
        List<IssuedBook> issuedBooks = issuedBookService.getAllIssuedBooks();
        model.addAttribute("issuedBooks", issuedBooks);
        return "issued_books";
    }

    // Return (Delete) an issued book (UNCHANGED)
    @GetMapping("/issued/return/{id}")
    public String returnBook(@PathVariable Long id) {
        issuedBookService.deleteIssuedBook(id);
        return "redirect:/admin/issued";
    }
}