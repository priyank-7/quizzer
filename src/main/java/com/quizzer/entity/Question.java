package com.quizzer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Question {

    @Id
    private String questionId;
    private String questionText;
    private String correctAnswer;
    private String hint;
    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER, mappedBy = "questions")
    private List<Option> options;
    @ManyToOne
    private Quiz quizzes;
}
