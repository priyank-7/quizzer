package com.quizzer.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionDto {

    private String uniqueOptionId;
    private String optionId;
    private String optionText;
}
