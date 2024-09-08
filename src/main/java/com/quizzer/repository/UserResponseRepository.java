package com.quizzer.repository;

import com.quizzer.entity.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserResponseRepository extends JpaRepository<UserResponse, String> {

    List<UserResponse> findAllBySubmisionId(String submisionId);
}
