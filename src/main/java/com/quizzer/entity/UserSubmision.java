package com.quizzer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserSubmision implements Serializable {

    @Id
    private String submisionId;
    private String userId;
    private String quizId;
    private float grade;
    private float marks;
    private Date submitedOn;
}
