package com.quizzer.repository;

import com.quizzer.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, String> {

    @Query(nativeQuery = true, value = "SELECT * FROM quiz WHERE grade = ?1")
    List<Quiz> findByGrade (String grade);

    @Query(nativeQuery = true, value = "SELECT * FROM quiz WHERE subject = ?1")
    List<Quiz> findBySubject (String subject);

    List<Quiz> findAllBySubject(String subject);

}
