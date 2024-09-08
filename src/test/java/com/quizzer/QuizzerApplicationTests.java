package com.quizzer;

import com.quizzer.repository.QuizRepository;
import com.quizzer.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuizzerApplicationTests {


    @Autowired
    private MailService mailService;

    @Autowired
    private QuizRepository quizRepository;

    @Test
    void contextLoads() {
        System.out.println(this.quizRepository.findById("6e29b350-6e16-4c4e-b649-e08a6957502b"));
    }
}
