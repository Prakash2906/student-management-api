package com.prakash.studentapi.service;

import com.prakash.studentapi.exception.StudentNotFoundException;
import com.prakash.studentapi.model.Student;
import com.prakash.studentapi.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> findAll(String course, String name) {
        if (course != null && !course.isBlank()) {
            return repository.findByCourseIgnoreCase(course);
        }
        if (name != null && !name.isBlank()) {
            return repository.findByNameContainingIgnoreCase(name);
        }
        return repository.findAll();
    }

    public Student findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Student create(Student student) {
        if (repository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + student.getEmail());
        }
        student.setId(null);
        return repository.save(student);
    }

    public Student update(Long id, Student changes) {
        Student existing = findById(id);
        if (!existing.getEmail().equalsIgnoreCase(changes.getEmail())
                && repository.existsByEmail(changes.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + changes.getEmail());
        }
        existing.setName(changes.getName());
        existing.setEmail(changes.getEmail());
        existing.setCourse(changes.getCourse());
        existing.setYear(changes.getYear());
        return repository.save(existing);
    }

    public void delete(Long id) {
        Student existing = findById(id);
        repository.delete(existing);
    }
}
