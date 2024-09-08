package com.quizzer.dto;

import com.quizzer.entity.Option;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {

    private String questionId;
    private String questionText;
    private List<OptionDto> options;
}
