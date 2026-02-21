package com.library.library_management.repository;

import com.library.library_management.model.BookRequest;
import com.library.library_management.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.library.library_management.model.RequestStatus;

public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
    List<BookRequest> findByStudent(Student student);
    List<BookRequest> findByStatus(RequestStatus status);
}