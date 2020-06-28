package com.energia.servicedesk.repository;

import com.energia.servicedesk.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}