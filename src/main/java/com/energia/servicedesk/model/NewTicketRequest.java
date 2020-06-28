package com.energia.servicedesk.model;

import com.energia.servicedesk.entity.enums.Priority;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class NewTicketRequest {

    @NotBlank
    @Size(min = 3, max = 128)
    private String title;

    @NotBlank
    @Email
    private String email;

    private String description;

    @NotNull
    private Priority priority;
}
