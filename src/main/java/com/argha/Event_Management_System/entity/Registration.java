package com.argha.Event_Management_System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Registration {
    @Id
    @Column(unique = true, name = "register_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendee_id")
    private User attendee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    private String status;

    @UpdateTimestamp
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
}
