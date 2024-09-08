package com.quizzer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Quiz {

    @Id
    private String quizId;
    private int grade;
    private String subject;
    private float totalQuestions;
    private float maxScore;
    private String difficulty;
    private Date createdOn;
    @OneToMany(fetch = jakarta.persistence.FetchType.EAGER, mappedBy = "quizzes")
    private List<Question> questions;

}
