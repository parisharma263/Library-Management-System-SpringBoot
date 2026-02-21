package com.library.library_management.service;

import com.library.library_management.model.IssuedBook;
import com.library.library_management.model.Student; // 👈 NEW IMPORT
import com.library.library_management.repository.IssuedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssuedBookService {

    @Autowired
    private IssuedBookRepository issuedBookRepository;

    @Autowired
    private com.library.library_management.repository.BookRepository bookRepository;

    public List<IssuedBook> getAllIssuedBooks() {
        return issuedBookRepository.findAll();
    }

    public void saveIssuedBook(IssuedBook issuedBook) {
        issuedBookRepository.save(issuedBook);
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteIssuedBook(Long id) {
        issuedBookRepository.findById(id).ifPresent(issuedBook -> {
            com.library.library_management.model.Book book = issuedBook.getBook();
            if (book != null) {
                book.setIssuedQuantity(Math.max(0, book.getIssuedQuantity() - 1));
                bookRepository.save(book);
            }
            issuedBookRepository.delete(issuedBook);
        });
    }

    // 👈 NEW METHOD: Logic for students to view their books
    public List<IssuedBook> getIssuedBooksByStudent(Student student) {
        return issuedBookRepository.findByStudent(student);
    }
}