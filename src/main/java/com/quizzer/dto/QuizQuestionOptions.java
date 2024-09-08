package com.quizzer.dto;

import lombok.*;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuizQuestionOptions {
    private String quizId;
    private int grade;
    private String subject;
    private float totalQuestions;
    private float maxScore;
    private String difficulty;
    private Date createdOn;
    private List<QuestionDto> questions;
}
