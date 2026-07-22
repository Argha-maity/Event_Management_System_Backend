package com.argha.Event_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "events")
public class User {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true)
    private String email;

    @Column(name = "contact_number", unique = true)
    private Long contactNumber;

    private String password;

    private String role;

    @Column(name = "is_verified")
    private Boolean isVerified=true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "organizer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Event> events;

    @OneToMany(mappedBy = "attendee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Registration> registrations;

    public void addEvent(Event event) {
        events.add(event);
        event.setOrganizer(this);
    }

    public void removeEvent(Event event) {
        events.remove(event);
        event.setOrganizer(null);
    }

    public void addRegistration(Registration registration) {
        registrations.add(registration);
        registration.setAttendee(this);
    }

    public void removeRegistration(Registration registration) {
        registrations.remove(registration);
        registration.setAttendee(null);
    }
}
