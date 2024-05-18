package com.project.OnlineScheduler.models;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppointmentRequest {
    private Long operatorId;
    @NonNull
    private String customerName;
    @NonNull
    private LocalTime startTime;
    @NonNull
    private LocalDate date;
}
