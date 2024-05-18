package com.project.OnlineScheduler.Service.impl;

import com.project.OnlineScheduler.Service.IAppointmentService;
import com.project.OnlineScheduler.models.AppointmentRequest;
import com.project.OnlineScheduler.models.AppointmentResponse;
import com.project.OnlineScheduler.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.OnlineScheduler.models.Appointment;
import org.springframework.util.CollectionUtils;

import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    private static final List<Long> VALID_OPERATOR_IDS = Arrays.asList(1L, 2L, 3L);

    @Override
    public AppointmentResponse bookAppointment(AppointmentRequest appointmentRequest) {
        try {
            if (Objects.isNull(appointmentRequest)) {
                throw new IllegalArgumentException("Appointment cannot be null");
            }
            if (Objects.nonNull(appointmentRequest.getOperatorId())) {
                if (!VALID_OPERATOR_IDS.contains(appointmentRequest.getOperatorId())) {
                    throw new IllegalArgumentException("Invalid Operator ID");
                } else if (checkIfOperatorAndTimeSlotAvailable(appointmentRequest)) {
                    return saveAndReturnAppointmentDetails(appointmentRequest);
                } else {
                    Appointment appointment = setAppointmentResponseForAppointmentNotBooked(appointmentRequest);
                    return new AppointmentResponse(appointment, "Operator is not available for the requested time slot and Date");
                }
            } else {
                Long operatorId = checkAndSelectAvailableOperator(appointmentRequest);
                if (Objects.isNull(operatorId)) {
                    Appointment appointment = setAppointmentResponseForAppointmentNotBooked(appointmentRequest);
                    return new AppointmentResponse(appointment, "Operator is not available for the requested time slot and Date");
                }
                appointmentRequest.setOperatorId(operatorId);
                return saveAndReturnAppointmentDetails(appointmentRequest);
            }
        } catch (IllegalArgumentException e) {
            return new AppointmentResponse(null, "Error: " + e.getMessage());
        }
    }

    @Override
    public AppointmentResponse rescheduleAppointment(Long appointmentId, AppointmentRequest newAppointment) {
        Appointment scheduledAppointment = appointmentRepository.findByAppointmentId(appointmentId);
        if (Objects.isNull(scheduledAppointment)) {
            return new AppointmentResponse(null, "Appointment not found, Please provide valid appointment id");
        }
        //cancel the appointment before rescheduling
        cancelAppointment(appointmentId);
        //book the new appointment
        AppointmentResponse appointmentResponse = bookAppointment(newAppointment);
        if (Objects.nonNull(appointmentResponse.getAppointment()) && Objects.nonNull(appointmentResponse.getAppointment().getStatus())
                && "BOOKED".equals(appointmentResponse.getAppointment().getStatus())) {
            appointmentResponse.setMessage("Your appointment has been rescheduled successfully with operator: " +
                    newAppointment.getOperatorId() + " and appointment id is: " + appointmentResponse.getAppointment().getAppointmentId());
        }
        return appointmentResponse;
    }

    @Override
    @Transactional
    public String cancelAppointment(Long appointmentId) {
        try {
            Appointment appointment = appointmentRepository.findByAppointmentId(appointmentId);
            if (Objects.isNull(appointment)) {
                return "Appointment not found, Please provide valid appointment id";
            }
            appointmentRepository.deleteByAppointmentId(appointmentId);
            return "Your Appointment with id: " + appointmentId + " has been cancelled successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private boolean checkIfOperatorAndTimeSlotAvailable(AppointmentRequest appointmentRequest) {
        List<Appointment> appointments = appointmentRepository.findByOperatorIdAndDate(appointmentRequest.getOperatorId(), appointmentRequest.getDate());

        //If appointments are present for the operator on the requested date, then check for overlapping time slots
        if (!CollectionUtils.isEmpty(appointments)) {
            LocalTime startTime = appointmentRequest.getStartTime();
            LocalTime endTime = appointmentRequest.getStartTime().plusHours(1);
            for (Appointment appointment : appointments) {
                if (appointment.getStartTime().equals(startTime) || (appointment.getStartTime().isBefore(endTime)
                        && appointment.getEndTime().isAfter(startTime))) {
                    return false;
                }
            }
        }
        return true;
    }

    private AppointmentResponse saveAndReturnAppointmentDetails(AppointmentRequest appointmentRequest) {
        Appointment newAppointment = new Appointment();
        newAppointment.setOperatorId(appointmentRequest.getOperatorId());
        newAppointment.setCustomerName(appointmentRequest.getCustomerName());
        newAppointment.setDate(appointmentRequest.getDate());
        newAppointment.setStartTime(appointmentRequest.getStartTime());
        newAppointment.setEndTime(appointmentRequest.getStartTime().plusHours(1));
        newAppointment.setStatus("BOOKED");
        Appointment appointmentDetails = appointmentRepository.save(newAppointment);
        newAppointment.setAppointmentId(appointmentDetails.getAppointmentId());
        return new AppointmentResponse(appointmentDetails, "Your appointment has been booked successfully with operator: " +
                newAppointment.getOperatorId() + " and appointment id is: " + newAppointment.getAppointmentId());
    }

    private Appointment setAppointmentResponseForAppointmentNotBooked(AppointmentRequest appointmentRequest) {
        Appointment appointment = new Appointment();
        appointment.setOperatorId(appointmentRequest.getOperatorId());
        appointment.setCustomerName(appointmentRequest.getCustomerName());
        appointment.setDate(appointmentRequest.getDate());
        appointment.setStartTime(appointmentRequest.getStartTime());
        appointment.setEndTime(appointmentRequest.getStartTime().plusHours(1));
        appointment.setStatus("NOT BOOKED");
        return appointment;
    }

    private Long checkAndSelectAvailableOperator(AppointmentRequest appointmentRequest) {
        LocalTime requestedStartTime = appointmentRequest.getStartTime();
        LocalTime requestedEndTime = requestedStartTime.plusHours(1);

        //check if the operator is available for the requested time slot
        for (Long operatorId : VALID_OPERATOR_IDS) {
            List<Appointment> operatorAppointments = appointmentRepository.findByOperatorIdAndDate(operatorId, appointmentRequest.getDate());
            boolean isSlotFree = true;
            for (Appointment appointment : operatorAppointments) {
                if ((appointment.getStartTime().isBefore(requestedEndTime) && appointment.getEndTime().isAfter(requestedStartTime)) ||
                        (appointment.getStartTime().equals(requestedStartTime))) {
                    isSlotFree = false;
                    break;
                }
            }
            if (isSlotFree) {
                return operatorId;
            }
        }
        return null;
    }
}
