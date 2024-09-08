package com.quizzer.service;


import com.quizzer.entity.User;
import com.quizzer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }


    public User getUserById(String user_id) {
        return this.userRepository.findById(user_id).orElseThrow(()-> new RuntimeException("User not found"));
    }


    public User addUser(User user) throws DataIntegrityViolationException {
        user.setUser_id(UUID.randomUUID().toString());

        return this.userRepository.save(user);
    }


    public void deleteUser(String user_id) {
        this.userRepository.deleteById(user_id);
    }


    public User updateUser(String user_id, User user) throws DataIntegrityViolationException {
        User tempUser = this.userRepository.findById(user_id).orElseThrow(()-> new RuntimeException("User not found"));
        tempUser.setEmail(user.getEmail());
        tempUser.setPassword(user.getPassword());
        return this.userRepository.save(tempUser); // unique constraint violation
    }

}
