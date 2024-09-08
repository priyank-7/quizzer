package com.quizzer.controller;


import com.quizzer.Security.JWT.JwtService;
import com.quizzer.dto.AuthRequest;
import com.quizzer.entity.Role;
import com.quizzer.entity.Roles;
import com.quizzer.entity.User;
import com.quizzer.repository.RoleRepository;
import com.quizzer.repository.UserRepository;
import com.quizzer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(JwtService jwtService, AuthenticationManager authenticationManager, UserService userService, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth")
    public String authAndGenerateToken(@RequestBody AuthRequest authRequest) throws BadCredentialsException {

        if(!userRepository.existsByEmail(authRequest.getUsername())){
            Role role = this.roleRepository.findByName(Roles.ROLE_ADMIN.toString()).orElseThrow(()-> new RuntimeException("Role not found"));
            User user = User.builder()
                    .user_id(UUID.randomUUID().toString())
                    .email(authRequest.getUsername())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .roles(Set.of(role))
                    .build();
            this.userRepository.save(user);
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(authRequest.getUsername());
        }
        else{
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @GetMapping("/getRole")
    public ResponseEntity<List<Role>> getRole(){
        return ResponseEntity.ok().build();
    }


    @GetMapping("/getall")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(this.userService.getAllUsers());
    }


}
