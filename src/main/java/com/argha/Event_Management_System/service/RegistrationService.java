package com.argha.Event_Management_System.service;

import com.argha.Event_Management_System.entity.Event;
import com.argha.Event_Management_System.entity.Registration;
import com.argha.Event_Management_System.entity.User;
import com.argha.Event_Management_System.repository.EventRepo;
import com.argha.Event_Management_System.repository.RegistrationRepo;
import com.argha.Event_Management_System.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired
    private RegistrationRepo registrationRepo;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private UserRepo userRepo;

    public Registration eventRegistration(User user, Event event){
        if(user==null || event==null)
            throw new RuntimeException("User or Event not found");

        User foundUser=userRepo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event foundEvent=eventRepo.findById(event.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Registration reg=new Registration();
        reg.setAttendee(user);
        reg.setEvent(event);
        reg.setStatus("WAITLISTED");
        registrationRepo.save(reg);
        foundUser.addRegistration(reg);
        foundEvent.addRegistration(reg);
        return reg;
    }

    public Registration getRegistrationById(Long id){
        if(id==null)
            throw new RuntimeException("Registration id not found");
        return  registrationRepo.findById(id).orElse(null);
    }

    public Registration confirmRegistrationService(Registration reg, Event event){
        if(reg==null || event==null)
            throw new RuntimeException("Registration or Event not found");

        reg.setStatus("CONFIRMED");
        event.setCurrentAttendee(event.getCurrentAttendee()+1);
        eventRepo.save(event);
        return reg;
    }

    public Registration cancelRegistrationService(Registration reg, Event event){
        if(reg==null || event==null)
            throw new RuntimeException("Registration or Event not found");

        reg.setStatus("CANCELLED");
        event.setCurrentAttendee(event.getCurrentAttendee()-1);
        eventRepo.save(event);
        return reg;
    }
}
