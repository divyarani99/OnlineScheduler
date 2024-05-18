package com.project.OnlineScheduler.Service;

import com.project.OnlineScheduler.models.Appointment;
import com.project.OnlineScheduler.models.AppointmentRequest;
import com.project.OnlineScheduler.models.AppointmentResponse;

public interface IAppointmentService {
    AppointmentResponse bookAppointment(AppointmentRequest appointmentRequest);

    AppointmentResponse rescheduleAppointment(Long appointmentId, AppointmentRequest newAppointment);

    String cancelAppointment(Long appointmentId);

}
