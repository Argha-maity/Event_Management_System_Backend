package com.argha.Event_Management_System.service;

import com.argha.Event_Management_System.entity.User;
import com.argha.Event_Management_System.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(@Lazy AuthenticationManager authManager, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private UserRepo userRepo;

//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AuthenticationManager authManager;

    public void registerUser(User user){
        if(user==null)
            throw new RuntimeException("User is null");

        User newUser=new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setContactNumber(user.getContactNumber());
        newUser.setRole(user.getRole());
        userRepo.save(newUser);
    }

    public User findUserByEmail(String email){
        if(email==null)
            throw new RuntimeException("email is null");
        return userRepo.findByEmail(email);
    }

    public User verifyUser(User user){
        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
                );
        if(authentication.isAuthenticated()){
            User foundUser=userRepo.findByEmail(user.getEmail());
            if(foundUser==null)
                throw  new RuntimeException("User not found");

            return foundUser;

        }else {
//            throw new RuntimeException("Authentication Failed");
            return null;
        }
    }
}
