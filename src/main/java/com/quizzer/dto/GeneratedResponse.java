package com.quizzer.dto;

import com.quizzer.entity.Question;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedResponse {
    private List<Question> questions;
}
