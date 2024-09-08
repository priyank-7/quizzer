package com.quizzer.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.quizzer.Security.Config.CustomUserDetails;
import com.quizzer.dto.*;
import com.quizzer.entity.*;
import com.quizzer.helper.OpenAi;
import com.quizzer.mapper.QuizQuestionOptionMapper;
import com.quizzer.repository.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {

    private final QuizRepository quizDao;
    private final QuestionRepository questionDao;
    private final OptionReposotory optionDao;
    private final UserSubmisionRepository userSubmisionDao;
    private final UserResponseRepository userResponseDao;
    private final  MailService mailService;
    private final OpenAi OpenAi;


    public QuizService(QuizRepository quizDao, QuestionRepository questionDao, OptionReposotory optionDao, UserSubmisionRepository userSubmisionDao, UserResponseRepository userResponseDao, MailService mailService, com.quizzer.helper.OpenAi openAi) {
        this.quizDao = quizDao;
        this.questionDao = questionDao;
        this.optionDao = optionDao;
        this.userSubmisionDao = userSubmisionDao;
        this.userResponseDao = userResponseDao;
        this.mailService = mailService;
        this.OpenAi = openAi;
    }


    public QuizQuestionOptions createQuiz(QuizDto quizDto, ChatClient chatClient) throws JsonProcessingException {
        List<Question> questionList = this.OpenAi.generateQuiz(quizDto, chatClient);
        System.out.println(questionList.toString());

        System.out.println("Quiz created successfully");

        Quiz quiz = Quiz.builder()
                .quizId(UUID.randomUUID().toString())
                .grade(quizDto.getGrade())
                .subject(quizDto.getSubject())
                .difficulty(quizDto.getDifficulty())
                .createdOn(new Date())
                .maxScore(quizDto.getMaxScore())
                .totalQuestions(quizDto.getTotalQuestions())
                .build();
        this.quizDao.save(quiz);

        for (Question question : questionList) {
            question.setQuizzes(quiz);
            question.setQuestionId(UUID.randomUUID().toString());
            this.questionDao.save(question);
        }

        for (Question question : questionList) {
            for (Option option : question.getOptions()) {
                option.setQuestions(question);
                option.setUniqueOptionId(UUID.randomUUID().toString());
                this.optionDao.save(option);
            }
        }
        return getQuizById(quiz.getQuizId());
    }

    public UserSubmision submitQuiz(QuizResponse questionResponse, Authentication authentication, ChatClient chatClient) throws JsonProcessingException {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        Quiz tempQuiz = this.quizDao.findById(questionResponse.getQuizId()).orElseThrow(() -> new RuntimeException("Quiz not found"));
        List<Question> questionList = this.questionDao.findByQuizzes(tempQuiz);

        UserSubmision userSubmision = UserSubmision.builder()
                .submisionId(UUID.randomUUID().toString())
                .userId(user.getUser().getUser_id())
                .quizId(tempQuiz.getQuizId())
                .submitedOn(new Date())
                .build();

        float totalQuestion = questionList.size();
        float score = 0;
        List<UserResponse> userResponses = new ArrayList<>();

        boolean flag = false;
        for(Question question : questionList){
            flag = false;
            for(QuestionAnswer qa : questionResponse.getAnswers()){
                if(qa.getQuestionId().equals(question.getQuestionId())){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                userResponses.add(
                        UserResponse.builder()
                                .questionResponseId(UUID.randomUUID().toString())
                                .questionId(question.getQuestionId())
                                .submisionId(userSubmision.getSubmisionId())
                                .response(null)
                                .isCorrect(false)
                                .build()
                );
            }

        }

        for (Question question : questionList) {
            for (QuestionAnswer qa : questionResponse.getAnswers()) {
                if (qa.getQuestionId().equals(question.getQuestionId())) {
                    if (qa.getAnswer().equals(question.getCorrectAnswer())) {
                        userResponses.add(
                                UserResponse.builder()
                                        .questionResponseId(UUID.randomUUID().toString())
                                        .questionId(question.getQuestionId())
                                        .submisionId(userSubmision.getSubmisionId())
                                        .response(qa.getAnswer())
                                        .isCorrect(true)
                                        .build()
                        );
                        score++;
                    } else {
                        userResponses.add(
                                UserResponse.builder()
                                        .questionResponseId(UUID.randomUUID().toString())
                                        .questionId(question.getQuestionId())
                                        .submisionId(userSubmision.getSubmisionId())
                                        .response(qa.getAnswer())
                                        .isCorrect(false)
                                        .build()
                        );
                    }
                }
            }
        }
        userSubmision.setMarks((score*tempQuiz.getMaxScore())/tempQuiz.getTotalQuestions());
        userSubmision.setGrade((score / tempQuiz.getTotalQuestions()) * 100);

        this.userSubmisionDao.save(userSubmision);
        this.userResponseDao.saveAll(userResponses);
        QuizQuestionOptions quiz = getQuizById(questionResponse.getQuizId());
        if(quiz == null){
            throw new RuntimeException("Quiz not found");
        }
        // TODO: send mail to user

        Suggestions suggestions = this.OpenAi.generateSuggestions(userSubmision.toString(), userResponses.toString(), quiz.toString(), chatClient);

        this.mailService.sendEmail(user.getUser().getEmail(), "Quiz Result", suggestions.getSuggestions().toString());
        return userSubmision;
    }

    public QuizQuestionOptions getQuizById(String quizId) {
        Quiz tempQuiz = this.quizDao.findById(quizId).orElse(null);
        if (tempQuiz == null) {
            throw new RuntimeException("Quiz not found");
        }

        List<Question> questionList = this.questionDao.findByQuizzes(tempQuiz);

        for (Question question : questionList) {
            List<Option> options = this.optionDao.findByQuestions(question);
            question.setOptions(options);
        }
        tempQuiz.setQuestions(questionList);

        return QuizQuestionOptionMapper.toQuizQuestionOptions(tempQuiz);
    }

    public List<UserRecord> getAllsubimission(Authentication authentication){

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        List<UserSubmision> submisionList = this.userSubmisionDao
                .findAllByUserId(user.getUser().getUser_id());

        List<UserRecord> userRecords = new ArrayList<>();

        for (UserSubmision submision: submisionList){
            List<UserResponse> userResponseList = this.userResponseDao.findAllBySubmisionId(submision.getSubmisionId());
            userRecords.add(
                    UserRecord.builder()
                            .userSubmision(submision)
                            .userResponses(userResponseList)
                            .build()
            );
        }
        return userRecords;
    }

    public List<UserRecord> getByGrade(float grade, Authentication authentication){

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        List<UserSubmision> submisionList = this.userSubmisionDao
                .findAllByUserIdAndGradeGreaterThanEqual(user.getUser().getUser_id(), grade);

        List<UserRecord> userRecords = new ArrayList<>();

        for (UserSubmision submision: submisionList){
            List<UserResponse> userResponseList = this.userResponseDao.findAllBySubmisionId(submision.getSubmisionId());
            userRecords.add(
                    UserRecord.builder()
                            .userSubmision(submision)
                            .userResponses(userResponseList)
                            .build()
            );
        }
        return userRecords;
    }

    public List<UserRecord> getBySubject(String Subject, Authentication authentication){

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        List<Quiz> quizList = this.quizDao.findAllBySubject(Subject);
        List<String> quizId = quizList.stream()
                .map(quiz -> new String(quiz.getQuizId()))
                .toList();

        List<UserSubmision> submisionList = this.userSubmisionDao
                .findAllByQuizIdInAndUserId(quizId, user.getUser().getUser_id());

        List<UserRecord> userRecords = new ArrayList<>();

        for (UserSubmision submision: submisionList){
            List<UserResponse> userResponseList = this.userResponseDao.findAllBySubmisionId(submision.getSubmisionId());
            userRecords.add(
                    UserRecord.builder()
                            .userSubmision(submision)
                            .userResponses(userResponseList)
                            .build()
            );
        }
        return userRecords;
    }

    public List<UserRecord> getByCompletedDate(Date completedDate, Authentication authentication){

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        List<UserSubmision> submisionList = this.userSubmisionDao
                .findAllByUserIdAndSubmitedOn(user.getUser().getUser_id(), completedDate);

        List<UserRecord> userRecords = new ArrayList<>();

        for (UserSubmision submision: submisionList){
            List<UserResponse> userResponseList = this.userResponseDao.findAllBySubmisionId(submision.getSubmisionId());
            userRecords.add(
                    UserRecord.builder()
                            .userSubmision(submision)
                            .userResponses(userResponseList)
                            .build()
            );
        }
        return userRecords;
    }

    public List<UserRecord> getByCompletedDateBetween(Date startDate, Date endDate, Authentication authentication){

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        List<UserSubmision> submisionList = this.userSubmisionDao
                .findBySubmissionDateBetween(startDate, endDate, user.getUser().getUser_id());

        List<UserRecord> userRecords = new ArrayList<>();

        for (UserSubmision submision: submisionList){
            List<UserResponse> userResponseList = this.userResponseDao.findAllBySubmisionId(submision.getSubmisionId());
            userRecords.add(
                    UserRecord.builder()
                            .userSubmision(submision)
                            .userResponses(userResponseList)
                            .build()
            );
        }
        return userRecords;
    }


    public Map<String, String> getHint(String quizId, String questionId) {
        Question question = this.questionDao.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found"));
        Map<String, String> map = new HashMap<>();
        map.put("quizId", quizId);
        map.put("questionId", questionId);
        map.put("question", question.getQuestionText());
        map.put("hint", question.getHint());
        return map;
    }
}
