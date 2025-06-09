package br.com.espacoconstruir.tutoring_backend.dto;

import lombok.Data;

@Data
public class BookingRequestDTO {
    private String date;        // YYYY-MM-DD format
    private String time;        // HH:mm format
    private String childId;     // Child's ID from database
    private String childName;   // Child's name
} 