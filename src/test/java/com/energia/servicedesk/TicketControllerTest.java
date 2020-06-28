package com.energia.servicedesk;

import com.energia.servicedesk.entity.enums.Priority;
import com.energia.servicedesk.entity.enums.Status;
import com.energia.servicedesk.model.NewTicketRequest;
import com.energia.servicedesk.model.TicketModel;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        addTicket(Priority.NORMAL, "first@gmail.com");
        addTicket(Priority.NORMAL, "second@gmail.com");
        addTicket(Priority.HIGH, "third@ex.com");
        addTicket(Priority.HIGH, "fourth@yahoo.com");
    }

    @Test
    public void get_all_tickets_OK() throws Exception {
        doGet("/api/tickets", 200);
    }

    @Test
    @Order(1)
    public void get_all_tickets_with_different_sorting() throws Exception {
        mockMvc.perform(get("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].email", is("first@gmail.com")))
                .andExpect(jsonPath("$[0].priority", is("NORMAL")))
                .andExpect(jsonPath("$[0].status", is("OPEN")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].email", is("second@gmail.com")))
                .andExpect(jsonPath("$[1].priority", is("NORMAL")))
                .andExpect(jsonPath("$[1].status", is("OPEN")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].email", is("third@ex.com")))
                .andExpect(jsonPath("$[2].priority", is("HIGH")))
                .andExpect(jsonPath("$[2].status", is("OPEN")))
                .andExpect(jsonPath("$[3].id", is(4)))
                .andExpect(jsonPath("$[3].email", is("fourth@yahoo.com")))
                .andExpect(jsonPath("$[3].priority", is("HIGH")))
                .andExpect(jsonPath("$[3].status", is("OPEN")));


        mockMvc.perform(get("/api/tickets?sortBy=priority&sortDirection=ASC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(3)))
                .andExpect(jsonPath("$[0].priority", is("HIGH")))
                .andExpect(jsonPath("$[1].id", is(4)))
                .andExpect(jsonPath("$[1].priority", is("HIGH")))
                .andExpect(jsonPath("$[2].id", is(1)))
                .andExpect(jsonPath("$[2].priority", is("NORMAL")))
                .andExpect(jsonPath("$[3].id", is(2)))
                .andExpect(jsonPath("$[3].priority", is("NORMAL")));

        mockMvc.perform(get("/api/tickets?sortBy=priority&sortDirection=DESC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].priority", is("NORMAL")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].priority", is("NORMAL")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].priority", is("HIGH")))
                .andExpect(jsonPath("$[3].id", is(4)))
                .andExpect(jsonPath("$[3].priority", is("HIGH")));
    }

    @Test
    public void post_one_ticket_status_201() throws Exception {
        NewTicketRequest ticketRequest = new NewTicketRequest();
        ticketRequest.setTitle("Bug1");
        ticketRequest.setEmail("sst@example.com");
        ticketRequest.setPriority(Priority.NORMAL);
        ticketRequest.setDescription("some description");

        MvcResult response = doPost("/api/tickets", ticketRequest, 201);

        TicketModel ticketModel = om.readValue(response.getResponse().getContentAsString(), TicketModel.class);

        assertThat(ticketModel.getTitle()).isEqualTo("Bug1");
        assertThat(ticketModel.getEmail()).isEqualTo("sst@example.com");
        assertThat(ticketModel.getPriority()).isEqualTo(Priority.NORMAL);
        assertThat(ticketModel.getDescription()).isEqualTo("some description");
        assertThat(ticketModel.getStatus()).isEqualTo(Status.OPEN);
    }

    @Test
    public void post_one_ticket_with_invalid_data_code_400() throws Exception {
        NewTicketRequest ticketRequest = new NewTicketRequest();
        ticketRequest.setTitle("1");

        doPost("/api/tickets", ticketRequest, 400);
    }

    @Test
    public void get_one_ticket_by_id() throws Exception {
        MvcResult result = doGet("/api/tickets/1", 200);
        TicketModel ticket = om.readValue(result.getResponse().getContentAsString(), TicketModel.class);
        assertThat(ticket.getEmail()).isEqualTo("first@gmail.com");
        assertThat(ticket.getPriority()).isEqualTo(Priority.NORMAL);
        assertThat(ticket.getStatus()).isEqualTo(Status.OPEN);
    }

    @Test
    public void return_404_for_one_non_existing_ticket() throws Exception {
        doGet("/api/tickets/9999", 404);
    }

    @Test
    public void update_ticket_OK() throws Exception {
        TicketModel ticketModel = postNewTicket("t1@mail.com", Priority.LOW);
        assert ticketModel.getStatus().equals(Status.OPEN);
        ticketModel.setStatus(Status.IN_PROGRESS);

        MvcResult result = doPut("/api/tickets/" + ticketModel.getId(), ticketModel, 200);
        TicketModel updatedTicket = om.readValue(result.getResponse().getContentAsString(), TicketModel.class);
        assert updatedTicket.getStatus().equals(Status.IN_PROGRESS);
    }

    @Test
    public void return_404_on_update_for_not_existing_ticket() throws Exception {
        TicketModel ticketModel = postNewTicket("t1ss@mail.com", Priority.LOW);
        doPut("/api/tickets/99999", ticketModel, 404);
    }

    private TicketModel postNewTicket(String email, Priority priority) throws Exception {
        String uuid = UUID.randomUUID().toString();
        NewTicketRequest ticketRequest = new NewTicketRequest();
        ticketRequest.setTitle("New " + uuid);
        ticketRequest.setEmail(email);
        ticketRequest.setPriority(priority);

        MvcResult ticketResult = doPost("/api/tickets", ticketRequest, 201);
        return om.readValue(ticketResult.getResponse().getContentAsString(), TicketModel.class);
    }

}
