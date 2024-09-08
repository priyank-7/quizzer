package com.quizzer.repository;

import com.quizzer.entity.Option;
import com.quizzer.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionReposotory extends JpaRepository<Option, String>{

    List<Option> findByQuestions(Question question);
}
