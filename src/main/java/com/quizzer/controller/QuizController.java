package com.quizzer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.quizzer.dto.QuizDto;
import com.quizzer.dto.QuizQuestionOptions;
import com.quizzer.dto.QuizResponse;
import com.quizzer.dto.UserRecord;
import com.quizzer.entity.UserSubmision;
import com.quizzer.service.QuizService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final ChatClient chatClient;

    public QuizController(QuizService quizService, ChatClient.Builder chatClient) {
        this.quizService = quizService;
        this.chatClient = chatClient.build();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createQuiz(@RequestBody QuizDto quizDto) throws JsonProcessingException {
            return ResponseEntity.ok(this.quizService.createQuiz(quizDto, chatClient));
    }

    @GetMapping("/getByQuizId")
    public ResponseEntity<QuizQuestionOptions> getQuizById(@RequestParam String quizId) {
        return ResponseEntity.ok(this.quizService.getQuizById(quizId));
    }


    @PostMapping("/submit")
    public ResponseEntity<UserSubmision> submitQuiz(@RequestBody QuizResponse questionResponses, Authentication authentication) throws JsonProcessingException {
        return ResponseEntity.ok(this.quizService.submitQuiz(questionResponses, authentication, chatClient));
    }


    @GetMapping("/byUser")
    public ResponseEntity<List<UserRecord>> getQuizByGrade(Authentication authentication){
        return ResponseEntity.ok(this.quizService.getAllsubimission(authentication));
    }

    @GetMapping("/byGrade")
    public ResponseEntity<List<UserRecord>> getQuizByGrade(@RequestParam float grade, Authentication authentication){
        return ResponseEntity.ok(this.quizService.getByGrade(grade, authentication));
    }

    @GetMapping("/bySubject")
    public ResponseEntity<List<UserRecord>> getQuizBySubject(@RequestParam String subject, Authentication authentication){
        return ResponseEntity.ok(this.quizService.getBySubject(subject, authentication));
    }

    @GetMapping("/byCompletedDate")
    public ResponseEntity<List<UserRecord>> getQuizByDate(@RequestParam Date completedDate, Authentication authentication){
        return ResponseEntity.ok(this.quizService.getByCompletedDate(completedDate, authentication));
    }

    @GetMapping("/byCompletedDateRange")
    public ResponseEntity<List<UserRecord>> getQuizByDateBetween(@RequestParam Date startDate, @RequestParam Date endDate, Authentication authentication){
        return ResponseEntity.ok(this.quizService.getByCompletedDateBetween(startDate, endDate, authentication));
    }

    @GetMapping("/hint")
    public ResponseEntity<Map<String, String>> getHint(@RequestParam String quizId, @RequestParam String questionId) {
        return ResponseEntity.ok(this.quizService.getHint(quizId, questionId));
    }
}
