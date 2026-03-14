package com.project.back_end.controllers;

import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TokenService tokenService;

    // GET all appointments (admin)
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments(
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateAdminToken(token)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    // GET appointments by doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getByDoctor(
            @PathVariable int doctorId,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    // GET appointments by doctor and date (daily report)
    @GetMapping("/doctor/{doctorId}/daily")
    public ResponseEntity<List<Appointment>> getDailyByDoctor(
            @PathVariable int doctorId,
            @RequestParam String date,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(appointmentService.getDailyAppointmentsByDoctor(doctorId, localDate));
    }

    // GET appointments by patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getByPatient(
            @PathVariable int patientId,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    // POST create appointment
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(
            @RequestBody AppointmentDTO dto,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validatePatientToken(token)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(appointmentService.createAppointment(dto));
    }

    // PUT update appointment status
    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable int id,
            @RequestParam String status,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        Appointment.AppointmentStatus appointmentStatus =
                Appointment.AppointmentStatus.valueOf(status);
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id, appointmentStatus));
    }

    // DELETE cancel appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(
            @PathVariable int id,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }
}
