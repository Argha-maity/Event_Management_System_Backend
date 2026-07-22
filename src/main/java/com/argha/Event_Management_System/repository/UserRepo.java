package com.argha.Event_Management_System.repository;

import com.argha.Event_Management_System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    public User findByEmail(String email);
}