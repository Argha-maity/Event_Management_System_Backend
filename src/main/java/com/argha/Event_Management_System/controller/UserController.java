package com.argha.Event_Management_System.controller;

import com.argha.Event_Management_System.entity.User;
import com.argha.Event_Management_System.security.UserPrincipal;
import com.argha.Event_Management_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserPrincipal userDetails){
        if(userDetails==null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You r not authenticated");

        User foundUser=userDetails.getUser();
        if(foundUser==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        return ResponseEntity.ok(foundUser);
    }
}
