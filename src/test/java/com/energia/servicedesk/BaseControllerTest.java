package com.energia.servicedesk;

import com.energia.servicedesk.entity.Ticket;
import com.energia.servicedesk.entity.enums.Priority;
import com.energia.servicedesk.model.NewTicketRequest;
import com.energia.servicedesk.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
public class BaseControllerTest {
    static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    void addTicket(Priority priority, String email) {

        String uuid = UUID.randomUUID().toString();
        NewTicketRequest ticketRequest = new NewTicketRequest();
        ticketRequest.setTitle("New " + uuid);
        ticketRequest.setEmail(email);
        ticketRequest.setPriority(priority);
        Ticket ticket = new Ticket(ticketRequest);

        ticketRepository.save(ticket);
    }

    MvcResult doGet(String url, int status) throws Exception {
        return mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(status))
                .andReturn();
    }

    MvcResult doPost(String url, Object body, int status) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body)))
                .andDo(print())
                .andExpect(status().is(status))
                .andReturn();
    }

    MvcResult doPut(String url, Object body, int status) throws Exception {
        return mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body)))
                .andDo(print())
                .andExpect(status().is(status))
                .andReturn();
    }
}