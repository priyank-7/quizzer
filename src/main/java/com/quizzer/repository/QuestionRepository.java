package com.quizzer.repository;

import com.quizzer.entity.Question;
import com.quizzer.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, String> {

    List<Question> findByQuizzes(Quiz quiz);
}
