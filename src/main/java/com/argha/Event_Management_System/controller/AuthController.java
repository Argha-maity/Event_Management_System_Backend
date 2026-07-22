package com.argha.Event_Management_System.controller;

import com.argha.Event_Management_System.entity.User;
import com.argha.Event_Management_System.security.JwtUtil;
import com.argha.Event_Management_System.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        try {
            if (user == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is null");

            User foundUser = authService.findUserByEmail(user.getEmail());
            if (foundUser != null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");

            authService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully\n" + user);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        try {
            if(user == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is null");

            User foundUser = authService.verifyUser(user);
            if(foundUser==null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid email or password");

            String token=jwtUtil.generateToken(foundUser.getEmail(), foundUser.getId());
            return ResponseEntity.status(HttpStatus.OK).body("User:    "+foundUser+"\nToken: "+token);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
