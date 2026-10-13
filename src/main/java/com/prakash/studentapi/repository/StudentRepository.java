package com.prakash.studentapi.repository;

import com.prakash.studentapi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    List<Student> findByCourseIgnoreCase(String course);

    List<Student> findByNameContainingIgnoreCase(String name);
}
