package com.argha.Event_Management_System.controller;

import com.argha.Event_Management_System.entity.Event;
import com.argha.Event_Management_System.entity.User;
import com.argha.Event_Management_System.security.UserPrincipal;
import com.argha.Event_Management_System.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Struct;
import java.util.Objects;

@RestController
@RequestMapping("/api/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@AuthenticationPrincipal UserPrincipal userDetails, @RequestBody Event event) {
        try{
            if(userDetails==null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("First need to login");

            if(event==null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event is null");

            User user = userDetails.getUser();
            if(user==null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            String role=user.getRole();
            if(!Objects.equals(role, "ORGANISER"))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not allowed to perform this action");
            Event newEvent=eventService.createEvent(event, user);
//            user.addEvent(newEvent);
            return ResponseEntity.status(HttpStatus.CREATED).body("New Event Draft successfully\n"+newEvent);
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> publishEvent(@AuthenticationPrincipal UserPrincipal userDetails, @RequestBody Event event){
        if(userDetails==null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("First need to login");
        if(event==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event is null");
        User user = userDetails.getUser();
        if(user==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        if(!Objects.equals(user.getRole(), "ORGANISER"))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not allowed to perform this action");

        Long id=event.getEventId();
        if(id==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id is null");
        String status=event.getStatus();
        if(status==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status is null");
        Event foundEvent=eventService.findEventById(id);
        if(foundEvent==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");

        Event updatedEvent=eventService.updateEventStatus(foundEvent,  status);
        return ResponseEntity.status(HttpStatus.OK).body(updatedEvent);
    }
}
