package com.project.OnlineScheduler.Service.impl;

import com.project.OnlineScheduler.Service.IOperatorService;
import com.project.OnlineScheduler.models.Appointment;
import com.project.OnlineScheduler.models.TimeSlot;
import com.project.OnlineScheduler.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

@Service
public class OperatorServiceImpl implements IOperatorService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public List<TimeSlot> getAllAppointmentTimeSlots(Long operatorId, LocalDate date) {
        List<TimeSlot> list = new ArrayList<>();
        List<Appointment> appointments = appointmentRepository.findByOperatorIdAndDate(operatorId, date);
        if (CollectionUtils.isEmpty(appointments)) {
            return null;
        }
        for (Appointment appointment : appointments) {
            if (appointment.getStatus().equals("BOOKED")) {
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setStartTime(appointment.getStartTime());
                timeSlot.setEndTime(appointment.getEndTime());
                list.add(timeSlot);
            }
        }
        Collections.sort(list, Comparator.comparing(TimeSlot::getStartTime));
        return list;
    }

    @Override
    public List<TimeSlot> getAvailableSlots(Long operatorId, LocalDate date) {
        // Fetch all appointments for the given operator and date
        List<Appointment> appointments = appointmentRepository.findByOperatorIdAndDate(operatorId, date);

        if (CollectionUtils.isEmpty(appointments)) {
            // If no appointments are booked, then all slots are available
            TimeSlot availableSlot = new TimeSlot();
            availableSlot.setStartTime(LocalTime.MIDNIGHT);
            availableSlot.setEndTime(LocalTime.of(23, 0));
            return List.of(availableSlot);
        }

        // Find all booked time slots
        List<TimeSlot> bookedSlots = new ArrayList<>();
        for (Appointment appointment : appointments) {
            TimeSlot bookedSlot = new TimeSlot();
            bookedSlot.setStartTime(appointment.getStartTime());
            bookedSlot.setEndTime(appointment.getEndTime());
            bookedSlots.add(bookedSlot);
        }

        // sort booked slots
        Collections.sort(bookedSlots, Comparator.comparing(TimeSlot::getStartTime));

        //we can adjust startTime and endTime as per the requirement
        LocalTime startTime = LocalTime.MIDNIGHT;
        LocalTime endTime = LocalTime.of(23, 0);

        List<TimeSlot> availableSlots = new ArrayList<>();
        for (TimeSlot bookedSlot : bookedSlots) {
            if (startTime.isBefore(bookedSlot.getStartTime())) {
                TimeSlot availableSlot = new TimeSlot();
                availableSlot.setStartTime(startTime);
                availableSlot.setEndTime(bookedSlot.getStartTime());
                availableSlots.add(availableSlot);
            }
            startTime = bookedSlot.getEndTime();
        }

        if (startTime.isBefore(endTime)) {
            TimeSlot availableSlot = new TimeSlot();
            availableSlot.setStartTime(startTime);
            availableSlot.setEndTime(endTime);
            availableSlots.add(availableSlot);
        }

        // Return the available slots
        return availableSlots;
    }
}
