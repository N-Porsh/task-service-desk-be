package com.energia.servicedesk.entity;

import com.energia.servicedesk.entity.enums.Priority;
import com.energia.servicedesk.entity.enums.Status;
import com.energia.servicedesk.model.NewTicketRequest;
import com.energia.servicedesk.model.TicketModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Data
@Table(name = "tickets")
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false)
    private String email;

    @Lob
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date created;

    @Column(nullable = false)
    @UpdateTimestamp
    @JsonIgnore
    private Date updated;

    public Ticket(NewTicketRequest request) {
        setTitle(request.getTitle());
        setEmail(request.getEmail().toLowerCase());
        setDescription(request.getDescription());
        setPriority(request.getPriority());
        setStatus(Status.OPEN);
    }

    public String getCreated() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return simpleDateFormat.format(created);
    }

    public Ticket update(TicketModel ticketModel) {
        setTitle(ticketModel.getTitle());
        setEmail(ticketModel.getEmail().toLowerCase());
        setDescription(ticketModel.getDescription());
        setPriority(ticketModel.getPriority());
        setStatus(ticketModel.getStatus());

        return this;
    }
}
