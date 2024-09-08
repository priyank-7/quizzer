package com.quizzer.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponse {

    private String quizId;
    private List<QuestionAnswer> answers;
}
