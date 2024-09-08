package com.quizzer.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {

    private String quizId;
    @NotNull(message = "Grade is required")
    private int grade;
    @NotNull(message = "Subject is required")
    private String subject;
    @NotNull(message = "Total number of questions is required")
    @Positive(message = "Total number of questions should be greater than 0")
    private int totalQuestions;
    @NotNull(message = "Maximum score is required")
    @PositiveOrZero(message = "Maximum score should be greater than or equal to 0")
    private float maxScore;
    private String difficulty;
}
