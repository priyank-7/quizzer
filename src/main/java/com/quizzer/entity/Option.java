package com.quizzer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
public class Option {

    @Id
    private String uniqueOptionId;
    private String optionId;
    private String optionText;
    @ManyToOne
    private Question questions;
}