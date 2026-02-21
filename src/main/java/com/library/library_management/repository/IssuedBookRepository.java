package com.library.library_management.repository;

import com.library.library_management.model.IssuedBook;
import com.library.library_management.model.Student; // 👈 NEW IMPORT
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssuedBookRepository extends JpaRepository<IssuedBook, Long> {

    // 👈 NEW METHOD: Spring Data JPA automatically generates the query
    // This finds all IssuedBook records linked to a specific Student entity.
    List<IssuedBook> findByStudent(Student student);
}