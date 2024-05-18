package com.project.OnlineScheduler.Controller;

import com.project.OnlineScheduler.Service.impl.AppointmentServiceImpl;
import com.project.OnlineScheduler.models.Appointment;
import com.project.OnlineScheduler.models.AppointmentRequest;
import com.project.OnlineScheduler.models.AppointmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentServiceImpl appointmentService;

    @PostMapping(value = "/bookAppointment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse bookAppointment(@RequestBody AppointmentRequest appointment) {
        return appointmentService.bookAppointment(appointment);
    }

    @PutMapping(value = "/{appointmentId}/rescheduleAppointment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AppointmentResponse rescheduleAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentRequest newAppointment) {
        return appointmentService.rescheduleAppointment(appointmentId, newAppointment);
    }

    @DeleteMapping(value = "/{appointmentId}/cancelAppointment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String cancelAppointment(@PathVariable Long appointmentId) {
        return appointmentService.cancelAppointment(appointmentId);
    }
}
