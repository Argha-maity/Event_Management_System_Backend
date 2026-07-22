package com.argha.Event_Management_System.controller;

import com.argha.Event_Management_System.entity.Event;
import com.argha.Event_Management_System.entity.Registration;
import com.argha.Event_Management_System.entity.User;
import com.argha.Event_Management_System.security.UserPrincipal;
import com.argha.Event_Management_System.service.EventService;
import com.argha.Event_Management_System.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EventService eventService;

    @PostMapping("/{eventId}")
    public ResponseEntity<?> registerForEvent(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable("eventId") long eventId){
        try {
            if (userPrincipal == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserPrincipal is null");
            if (eventId <= 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EventId is null");

            User user = userPrincipal.getUser();
            if (user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            if (!Objects.equals(user.getRole(), "ATTENDEE"))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User must be a ATTENDEE");

            Event foundEvent = eventService.findEventById(eventId);
            if (foundEvent == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");

            Registration reg = registrationService.eventRegistration(user, foundEvent);
            if (reg == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration not happened");
            return ResponseEntity.status(HttpStatus.CREATED).body(reg);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/confirm/{registrationId}")
    public ResponseEntity<?> confirmRegistration(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable("registrationId") long registrationId){
        try {
            if (userPrincipal == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserPrincipal is null");
            if (registrationId <= 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("RegistrationId is null");

            User user = userPrincipal.getUser();
            if (user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            if (!Objects.equals(user.getRole(), "ORGANISER"))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User must be a ORGANISER");

            Registration reg = registrationService.getRegistrationById(registrationId);
            if (reg == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registration not found");

            Event event = reg.getEvent();
            if (event == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");

            User attendee = reg.getAttendee();
            if (attendee == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Attendee found");

            if(event.getCurrentAttendee()>=event.getMaxCapacity())
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Maximum Capacity Exceeded");

            Registration updatedReg=registrationService.confirmRegistrationService(reg, event);
            if (updatedReg == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
            return ResponseEntity.status(HttpStatus.OK).body(updatedReg);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/cancel/{registrationId}")
    public ResponseEntity<?> cancelRegistration(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable("registrationId") long registrationId){
        try{
            if (userPrincipal == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UserPrincipal is null");
            if (registrationId <= 0)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("RegistrationId is null");

            User user = userPrincipal.getUser();
            if (user == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

            if (!Objects.equals(user.getRole(), "ORGANISER"))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User must be a ORGANISER");

            Registration reg = registrationService.getRegistrationById(registrationId);
            if (reg == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registration not found");

            Event event = reg.getEvent();
            if (event == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");

            User attendee = reg.getAttendee();
            if (attendee == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Attendee found");

            Registration updatedReg=registrationService.cancelRegistrationService(reg, event);
            if (updatedReg == null)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
            return ResponseEntity.status(HttpStatus.OK).body(updatedReg);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
