package com.project.OnlineScheduler.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonNull
    private Long appointmentId;
    private String customerName;
    private Long operatorId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private String status;
}
