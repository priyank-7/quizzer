package com.quizzer.mapper;

import com.quizzer.dto.OptionDto;
import com.quizzer.dto.QuestionDto;
import com.quizzer.dto.QuizQuestionOptions;
import com.quizzer.entity.Option;
import com.quizzer.entity.Question;
import com.quizzer.entity.Quiz;

import java.util.stream.Collectors;

public class QuizQuestionOptionMapper {

    public static QuizQuestionOptions toQuizQuestionOptions(Quiz quiz) {
       return QuizQuestionOptions.builder()
               .quizId(quiz.getQuizId())
               .totalQuestions(quiz.getTotalQuestions())
               .difficulty(quiz.getDifficulty())
               .subject(quiz.getSubject())
               .grade(quiz.getGrade())
               .maxScore(quiz.getMaxScore())
               .createdOn(quiz.getCreatedOn())
               .questions(quiz.getQuestions().stream().map(QuizQuestionOptionMapper::toQuestionDto).collect(Collectors.toList()))
               .build();
    }

    private static QuestionDto toQuestionDto(Question question) {
        return QuestionDto.builder()
                .questionId(question.getQuestionId())
                .questionText(question.getQuestionText())
                .options(question.getOptions().stream().map(QuizQuestionOptionMapper::toOptionDto).collect(Collectors.toList()))
                .build();
    }

    private static OptionDto toOptionDto(Option option) {
        return OptionDto.builder()
                .optionId(option.getOptionId())
                .optionText(option.getOptionText())
                .build();
    }
}
