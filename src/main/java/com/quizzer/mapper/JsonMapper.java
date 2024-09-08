package com.quizzer.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizzer.dto.GeneratedResponse;
import com.quizzer.dto.Suggestions;
import com.quizzer.entity.Question;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonMapper {
    public static GeneratedResponse mapJsonToQuizApiResponse(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, GeneratedResponse.class);
    }
    public static Suggestions mapJsonToSuggestionsResponse(String jsonString) throws JsonProcessingException {
        Pattern pattern = Pattern.compile("\\{.*\\}");
        Matcher matcher = pattern.matcher(jsonString);
        String json = "";
        if (matcher.find()) {
            json = matcher.group();
        }

        // Parse the JSON string using Jackson
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Suggestions.class);
    }
}



