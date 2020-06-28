package com.energia.servicedesk.controller;

import com.energia.servicedesk.entity.Ticket;
import com.energia.servicedesk.model.NewTicketRequest;
import com.energia.servicedesk.model.TicketModel;
import com.energia.servicedesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService service;

    @GetMapping()
    public List<Ticket> getAllTickets(@RequestParam(defaultValue = "created") String sortBy,
                                      @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection) {
        return service.getAllNotClosedTickets(sortBy, sortDirection);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTicket(@PathVariable Long id) {
        return service.getTicket(id);
    }

    @PostMapping()
    public ResponseEntity addNewTicket(@Valid @RequestBody NewTicketRequest request) {
        return service.addTicket(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTicket(@RequestBody TicketModel request, @PathVariable long id) {
        return service.updateTicket(request, id);
    }
}
