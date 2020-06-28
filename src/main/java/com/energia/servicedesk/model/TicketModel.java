package com.energia.servicedesk.model;

import com.energia.servicedesk.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TicketModel extends NewTicketRequest {

    private Long id;

    @NotNull
    private Status status;

    @JsonIgnore
    private Date created;
}
