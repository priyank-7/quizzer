package com.quizzer.repository;

import com.quizzer.entity.UserSubmision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface UserSubmisionRepository extends JpaRepository<UserSubmision, String> {

    @Query("SELECT s FROM UserSubmision s WHERE s.userId = ?1")
    List<UserSubmision> findAllByUserId(String useId);
    List<UserSubmision> findAllByUserIdAndGradeGreaterThanEqual(String userId, float grade);

    List<UserSubmision> findAllByQuizIdInAndUserId (List<String> quizIds, String userID);

    @Query("SELECT u FROM UserSubmision u WHERE DATE(u.submitedOn) = DATE( :submitedOn) AND u.userId = :userID")
    List<UserSubmision> findAllByUserIdAndSubmitedOn(String userID, Date submitedOn);

    @Query("SELECT u FROM UserSubmision u WHERE DATE(u.submitedOn) BETWEEN DATE( :startDate) AND DATE( :endDate) AND u.userId = :userId")
    List<UserSubmision> findBySubmissionDateBetween(Date startDate, Date endDate, String userId);

}