package com.quizzer.dto;

import com.quizzer.entity.UserResponse;
import com.quizzer.entity.UserSubmision;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRecord implements Serializable {

    private UserSubmision userSubmision;
    private List<UserResponse> userResponses;
}
