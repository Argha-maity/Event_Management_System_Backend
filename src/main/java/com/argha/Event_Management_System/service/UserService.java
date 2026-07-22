package com.argha.Event_Management_System.service;

import com.argha.Event_Management_System.entity.User;
import com.argha.Event_Management_System.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public User findUserByEmail(String email){
        if(email==null)
            throw new RuntimeException("email is null");
        return userRepo.findByEmail(email);
    }
}
