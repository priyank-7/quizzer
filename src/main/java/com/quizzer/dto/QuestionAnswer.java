package com.quizzer.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswer {

    private String questionId;
    private String answer;
}
