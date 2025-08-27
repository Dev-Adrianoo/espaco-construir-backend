package br.com.espacoconstruir.tutoring_backend.dto;

import lombok.Data;

@Data
public class CancellationRequestDTO {
    private long scheduleId;
    private String scope;
}
