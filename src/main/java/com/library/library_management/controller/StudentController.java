package com.library.library_management.controller;

import com.library.library_management.model.Book; // Not used in final code, but kept for safety
import com.library.library_management.model.IssuedBook;
import com.library.library_management.model.Student;
import com.library.library_management.model.User;
import com.library.library_management.model.BookRequest; // NEW: Needed for viewMyRequests
import com.library.library_management.service.BookService; // Not used in final code, but kept for safety
import com.library.library_management.service.IssuedBookService;
import com.library.library_management.service.UserService;
import com.library.library_management.service.BookRequestService; // NEW: Inject the service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Kept for viewMyIssuedBooks
import org.springframework.security.core.userdetails.UserDetails; // Kept for viewMyIssuedBooks
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal; // NEW: Used in viewMyRequests
import java.util.List;

@Controller
@RequestMapping("/student")
@PreAuthorize("hasRole('STUDENT')") // Secures all methods for Students
public class StudentController {

    private final UserService userService;
    private final IssuedBookService issuedBookService;
    private final BookRequestService bookRequestService; // 1. ADD NEW SERVICE FIELD
    // private final BookService bookService; // Removed as it's not directly used here anymore

    // 2. UPDATE CONSTRUCTOR to inject BookRequestService
    @Autowired
    public StudentController(UserService userService, IssuedBookService issuedBookService,
                             BookRequestService bookRequestService) {
        this.userService = userService;
        this.issuedBookService = issuedBookService;
        this.bookRequestService = bookRequestService; // Initialize the new service
    }

    // Student Dashboard
    @GetMapping("/dashboard")
    public String studentDashboard() {
        return "student-dashboard";
    }

    // ⛔ REMOVED: The old direct issue method (/student/issue/{bookId}) is removed.
    //             Issuance is now handled by Admin approval of a request.

    // Student ki issued books dekhne ka page (Kept as is)
    @GetMapping("/issued-books")
    public String viewMyIssuedBooks(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User currentUser = userService.findByUsername(username);
        Student currentStudent = currentUser.getStudent();

        List<IssuedBook> myBooks = issuedBookService.getIssuedBooksByStudent(currentStudent);
        model.addAttribute("issuedBooks", myBooks);

        // NOTE: Ensure your service has getIssuedBooksByStudent(Student) implemented.
        return "my-issued-books";
    }

    // 3. FINALIZED NEW METHOD: View Student's Book Requests
    // The @GetMapping annotation must use the relative path "/requests" since the class uses @RequestMapping("/student")
    // The previous code had a redundant @PreAuthorize and used the wrong path in the GetMapping.
    @GetMapping("/requests") // Corrected path: /student/requests
    public String viewMyRequests(Model model, Principal principal) {
        // 1. Get the authenticated student user
        String username = principal.getName();
        User user = userService.findByUsername(username);
        Student student = user.getStudent(); // Correctly linked via User.getStudent()

        // 2. Fetch all requests for this specific student (Requires findByStudent in repository)
        List<BookRequest> myRequests = bookRequestService.findByStudent(student);

        model.addAttribute("requests", myRequests);
        return "student-requests"; // Template name
    }

    // NEW: Student Self-Return Method
    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        // Calls the shared service logic which decrements issuedQuantity
        issuedBookService.deleteIssuedBook(id);
        return "redirect:/student/issued-books?success=returned";
    }
}