package com.quizzer.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quizzer.dto.GeneratedResponse;
import com.quizzer.dto.QuizDto;
import com.quizzer.dto.Suggestions;
import com.quizzer.entity.Question;
import com.quizzer.mapper.JsonMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpenAi {

    public List<Question> generateQuiz(QuizDto quizDto, ChatClient chatClient) throws JsonProcessingException {
        String createdQuestion = """
                Generate a quiz based on the following requirements:
                                
                        - Grade: %d
                        - Subject: %s
                        - Number of Questions: %d
                        - Difficulty Level: %s
                                
                        For each question, provide the following details:
                        1. The quiz question.
                        2. multiple-choice options (labeled A, B, C, etc.).
                        3. Indicate the correct answer from the provided options.
                        4. A hint to assist the student in finding the correct answer. But answer should not be exposed in the hint.
                                
                        Ensure that:
                        - The questions are appropriate for the grade level and subject.
                        - The quiz reflects the specified difficulty level (%s).
                        - Options should be clearly distinguishable, and only one is correct.
                                
                        For each question:
                        1. Provide the question text.
                        2. Provide a list of options for the question. Each option should be formatted as:
                           - "optionText": "option text"
                                
                        Format the entire quiz as JSON with the following structure:
                                
                        {
                          "questions": [
                            {
                              "questionText": "Question text here",
                              "options": [
                                {"optionId": "A", "optionText": "Option 1 text"},
                                {"optionId": "B", "optionText": "Option 2 text"},
                                {"optionId": "C", "optionText": "Option 3 text"},
                                {"optionId": "D", "optionText": "Option 4 text"}
                              ]
                            },
                            ...
                          ]
                        }
                                
                        In response only return json string, no extra lines or characters.
                """
                .formatted(quizDto.getGrade(), quizDto.getSubject(), quizDto.getTotalQuestions(), quizDto.getDifficulty(), quizDto.getDifficulty());
        var response = chatClient.prompt()
                .user(createdQuestion)
                .call()
                .content();
        System.out.println(response);
        System.out.println("Response received");
        GeneratedResponse quizApiResponse = JsonMapper.mapJsonToQuizApiResponse(response);
        System.out.println("Json mapped");
        if(quizApiResponse == null) {
            throw new RuntimeException("Error processing response");
        }
        return quizApiResponse.getQuestions();
    }

    public Suggestions generateSuggestions(String userSubmission, String userResponse, String quiz, ChatClient chatClient) throws JsonProcessingException {
        String createdQuestion = """
                Analyze the following quiz data:
                                                                                          
                    Quiz Information:
                    
                        - Quiz Questions: %s
                        - User Response: %s
                        - User Submission: %s
                        
                        
                        Quiz contains all the questions with their options and answers to each question. Only answers were not provided to the user.
                        User Response contains the user's answers to the quiz questions.
                        User Submission contains the user's performance on the quiz.
                                                                                          
                        Based on the quiz performance and the given quiz details, provide two suggestions for improvement or If no need of improvement, then suggest two new topics to learn.
                        
                        Format of the response:
                        {
                            "suggestions": [
                                "Suggestion 1",
                                "Suggestion 2"
                            ]
                        }
                        In response only return json string, no extra lines or characters.
                """
                .formatted(quiz, userResponse, userSubmission);
        var response = chatClient.prompt()
                .user(createdQuestion)
                .call()
                .content();

        System.out.println(response);
        System.out.println("Response received");
        Suggestions suggestions = JsonMapper.mapJsonToSuggestionsResponse(response);
        System.out.println("Json mapped");
        if(suggestions == null) {
            throw new RuntimeException("Error processing response");
        }
        return suggestions;
    }
}
