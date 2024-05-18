package com.project.OnlineScheduler.Controller;

import com.project.OnlineScheduler.Service.impl.OperatorServiceImpl;
import com.project.OnlineScheduler.models.Appointment;
import com.project.OnlineScheduler.models.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/operators")
public class OperatorController {
    @Autowired
    private OperatorServiceImpl operatorService;

    @GetMapping(value = "/{operatorId}/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<TimeSlot> getAllAppointmentTimeSlots(@PathVariable Long operatorId, @RequestParam LocalDate date) {
        return operatorService.getAllAppointmentTimeSlots(operatorId, date);
    }

    @GetMapping(value = "/{operatorId}/availableSlots", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<TimeSlot> getAvailableSlots(@PathVariable Long operatorId, @RequestParam LocalDate date) {
        return operatorService.getAvailableSlots(operatorId, date);
    }
}
