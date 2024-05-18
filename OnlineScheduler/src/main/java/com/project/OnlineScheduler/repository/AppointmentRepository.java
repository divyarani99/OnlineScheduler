package com.project.OnlineScheduler.repository;

import com.project.OnlineScheduler.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    void deleteByAppointmentId(Long appointmentId);

    Appointment findByAppointmentId(Long appointmentId);

    List<Appointment> findByOperatorIdAndDate(Long operatorId, LocalDate date);

}
