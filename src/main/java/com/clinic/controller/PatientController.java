package com.project.back_end.controllers;

import com.project.back_end.dto.LoginRequest;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TokenService tokenService;

    // POST patient login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Map<String, String> response = new HashMap<>();
        Optional<Patient> patient = patientService.getPatientByUsername(request.getUsername());

        if (patient.isPresent() && patientService.authenticatePatient(request.getUsername(), request.getPassword())) {
            String token = tokenService.generateToken(request.getUsername(), "PATIENT");
            response.put("token", token);
            response.put("role", "PATIENT");
            response.put("name", patient.get().getFullName());
            response.put("patientId", String.valueOf(patient.get().getPatientId()));
            return ResponseEntity.ok(response);
        }
        response.put("error", "Invalid credentials");
        return ResponseEntity.status(401).body(response);
    }

    // POST register new patient
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Patient patient) {
        patientService.savePatient(patient);
        return ResponseEntity.ok("Patient registered successfully");
    }

    // GET patient appointments
    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<Appointment>> getPatientAppointments(
            @PathVariable int id,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(id));
    }

    // GET all patients (admin only)
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients(
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateAdminToken(token)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(patientService.getAllPatients());
    }
}
