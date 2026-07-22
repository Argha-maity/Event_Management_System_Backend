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
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "organizer")
public class Event {
    @Id
    @Column(unique = true, name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;

    @Column(length = 1000)
    private String description;
    private String venue;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "max_capacity")
    private Integer maxCapacity;
    @Column(name = "current_attendee")
    private Integer currentAttendee=0;
    private Double price;
    private String status="DRAFT";
    private Boolean isActive=true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Registration> registrations;

    public void addRegistration(Registration registration){
        registrations.add(registration);
        registration.setEvent(this);
    }

    public void removeRegistration(Registration registration){
        registrations.remove(registration);
        registration.setEvent(null);
    }
}