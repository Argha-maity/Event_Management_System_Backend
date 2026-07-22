package com.argha.Event_Management_System.service;

import com.argha.Event_Management_System.entity.Event;
import com.argha.Event_Management_System.entity.User;
import com.argha.Event_Management_System.repository.EventRepo;
import com.argha.Event_Management_System.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EventService {
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private UserRepo userRepo;

    public Event createEvent(Event event, User user) {
        if(event==null)
            throw new RuntimeException("Event is null");

        User managedUser=userRepo.findById(user.getId())
                .orElseThrow(()->new RuntimeException("user not found"));

        Event newEvent=new Event();
        newEvent.setOrganizer(user);
        newEvent.setTitle(event.getTitle());
        newEvent.setDescription(event.getDescription());
        newEvent.setVenue(event.getVenue());
        newEvent.setEventDate(event.getEventDate());
        newEvent.setMaxCapacity(event.getMaxCapacity());
        newEvent.setPrice(event.getPrice());
        newEvent.setStatus(event.getStatus());

        eventRepo.save(newEvent);
        managedUser.addEvent(newEvent);
        return newEvent;
    }

    public Event updateEventStatus(Event event, String newStatus) {
        if(event==null)
            throw new RuntimeException("Event is null");
        if(newStatus=="")
            throw new RuntimeException("New status is empty");

        event.setStatus(newStatus);
        eventRepo.save(event);
        return event;
    }

    public User findOrganizer(Event event) {
        if(event==null)
            throw new RuntimeException("Event is null");
        return userRepo.findById(event.getOrganizer().getId()).
                orElse(null);
    }

    public Event findEventById(Long id) {
        if(id==null)
            throw new RuntimeException("Id is null");
        return eventRepo.findById(id).orElse(null);
    }
}
