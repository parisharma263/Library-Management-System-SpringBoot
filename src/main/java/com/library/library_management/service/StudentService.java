package com.library.library_management.service;

import com.library.library_management.model.Student;
import com.library.library_management.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Save a student (new or updated)
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    // Get a student by ID
    public Student getStudentById(Long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        return optionalStudent.orElse(null);  // returns null if not found
    }

    // Delete a student by ID
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
