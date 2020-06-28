package com.energia.servicedesk.service;

import com.energia.servicedesk.entity.Ticket;
import com.energia.servicedesk.entity.enums.Status;
import com.energia.servicedesk.model.NewTicketRequest;
import com.energia.servicedesk.model.TicketModel;
import com.energia.servicedesk.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class TicketService {

    private final TicketRepository repository;

    public List<Ticket> getAllNotClosedTickets(String sortBy, Sort.Direction sortDirection) {
        return repository.findAll(Sort.by(sortDirection, sortBy))
                .stream()
                .filter(t -> t.getStatus() != Status.CLOSED)
                .collect(Collectors.toList());
    }

    public Ticket getById(Long id) {
        Optional<Ticket> ticket = repository.findById(id);
        return ticket.orElse(null);
    }

    public ResponseEntity getTicket(Long id) {
        Ticket ticket = getById(id);

        if (ticket == null) {
            log.info("Ticket not found. Requested id:{}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket);
    }

    public ResponseEntity addTicket(NewTicketRequest request) {
        Ticket newTicket = new Ticket(request);
        log.info("Adding new ticket");
        Ticket ticket = repository.save(newTicket);

        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    public ResponseEntity updateTicket(TicketModel ticketRequest, long id) {
        Ticket ticket = getById(id);

        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("updating ticket id:{}", ticket.getId());

        ticket = ticket.update(ticketRequest);
        repository.save(ticket);

        return ResponseEntity.status(HttpStatus.OK).body(ticket);
    }
}
