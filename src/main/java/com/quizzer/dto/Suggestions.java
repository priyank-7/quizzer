package com.quizzer.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Suggestions {
    List<String> suggestions;
}
