package com.library.library_management.service;

import com.library.library_management.model.Book;
import com.library.library_management.model.BookRequest;
import com.library.library_management.model.IssuedBook;
import com.library.library_management.model.RequestStatus;
import com.library.library_management.model.Student;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.BookRequestRepository;
import com.library.library_management.repository.IssuedBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BookRequestService {
    // ... (Constructor and fields remain the same) ...

    private final BookRequestRepository requestRepository;
    private final IssuedBookRepository issuedBookRepository;
    private final BookRepository bookRepository;

    public BookRequestService(BookRequestRepository requestRepository, IssuedBookRepository issuedBookRepository, BookRepository bookRepository) {
        this.requestRepository = requestRepository;
        this.issuedBookRepository = issuedBookRepository;
        this.bookRepository = bookRepository;
    }

    // --- STUDENT METHOD (createRequest) ---
    public BookRequest createRequest(Book book, Student student) {
        BookRequest request = new BookRequest();
        request.setBook(book);
        request.setStudent(student);
        return requestRepository.save(request);
    }

    // --- ADMIN: Fetch all pending requests ---
    public List<BookRequest> findAllPendingRequests() {
        return requestRepository.findByStatus(RequestStatus.PENDING);
    }

    // --- ADMIN: Approve a request ---
    @Transactional
    public boolean approveRequest(Long requestId) {
        BookRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        Book book = request.getBook();

        // 1. Safety check: Is the book available?
        if (book.getAvailableQuantity() <= 0) {
            // If out of stock, we just reject it and return false
            request.setStatus(RequestStatus.REJECTED);
            requestRepository.save(request);
            return false;
        }

        // 2. Create the IssuedBook record
        IssuedBook issuedBook = new IssuedBook(request.getStudent(), book);
        issuedBookRepository.save(issuedBook);

        // 3. Update the Book's inventory
        book.setIssuedQuantity(book.getIssuedQuantity() + 1);
        bookRepository.save(book);

        // 4. Update the Request status
        request.setStatus(RequestStatus.ISSUED);
        requestRepository.save(request);

        return true;
    }

    // --- ADMIN: Reject a request ---
    @Transactional
    public boolean rejectRequest(Long requestId) {
        Optional<BookRequest> optionalRequest = requestRepository.findById(requestId);

        if (optionalRequest.isPresent()) {
            BookRequest request = optionalRequest.get();
            request.setStatus(RequestStatus.REJECTED);
            requestRepository.save(request);
            return true; // Success
        }
        return false; // Request not found
    }

    // --- STUDENT: Fetch all requests for a specific student ---
    public List<BookRequest> findByStudent(Student student) {
        return requestRepository.findByStudent(student);
    }
}