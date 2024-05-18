package com.project.OnlineScheduler.Service;

import com.project.OnlineScheduler.models.Appointment;
import com.project.OnlineScheduler.models.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface IOperatorService {
    List<TimeSlot> getAllAppointmentTimeSlots(Long operatorId, LocalDate date);

    List<TimeSlot> getAvailableSlots(Long operatorId, LocalDate date);
}
