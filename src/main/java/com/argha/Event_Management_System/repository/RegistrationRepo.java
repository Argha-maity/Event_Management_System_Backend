package com.argha.Event_Management_System.repository;

import com.argha.Event_Management_System.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegistrationRepo extends JpaRepository<Registration, Long> {
    @Query("SELECT r FROM Registration r WHERE r.registrationId=:id")
    public Registration findById(long id);
}
