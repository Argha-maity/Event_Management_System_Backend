package com.argha.Event_Management_System.repository;

import com.argha.Event_Management_System.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepo extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.eventId=:id")
    public Event findById(long id);
}
