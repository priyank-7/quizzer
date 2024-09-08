package com.quizzer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserResponse implements Serializable {

    @Id
    private String questionResponseId;
    private String questionId;
    private String submisionId;
    private String response;
    private boolean isCorrect;
}
