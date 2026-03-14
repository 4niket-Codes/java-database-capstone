package com.project.back_end.controllers;

import com.project.back_end.dto.LoginRequest;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired private DoctorService doctorService;
    @Autowired private TokenService tokenService;

    /** GET all doctors — requires any valid token */
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors(
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    /** GET single doctor by ID */
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) return ResponseEntity.status(401).build();
        return doctorService.getDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET doctor availability by role, doctor ID, date, and token.
     * Retrieves available time slots for a specific doctor filtered by role and date.
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable Long id,
            @RequestParam String date,
            @RequestParam String role,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) return ResponseEntity.status(401).build();
        List<String> slots = doctorService.getAvailableTimeSlots(id, date, role);
        Map<String, Object> response = new HashMap<>();
        response.put("doctorId", id);
        response.put("date", date);
        response.put("role", role);
        response.put("availableSlots", slots);
        return ResponseEntity.ok(response);
    }

    /** GET doctors by specialization and optional time filter */
    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialityAndTime(
            @RequestParam String speciality,
            @RequestParam(required = false) String time,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialityAndTime(speciality, time));
    }

    /** POST doctor login */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Map<String, String> response = new HashMap<>();
        if (doctorService.validateDoctorLogin(request.getUsername(), request.getPassword())) {
            String token = tokenService.generateToken(request.getUsername(), "DOCTOR");
            response.put("token", token);
            response.put("role", "DOCTOR");
            return ResponseEntity.ok(response);
        }
        response.put("error", "Invalid credentials");
        return ResponseEntity.status(401).body(response);
    }

    /** POST add doctor — admin only */
    @PostMapping
    public ResponseEntity<Map<String, String>> addDoctor(
            @RequestBody Doctor doctor,
            @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        if (!tokenService.validateAdminToken(token)) {
            response.put("error", "Forbidden: Admins only");
            return ResponseEntity.status(403).body(response);
        }
        doctorService.saveDoctor(doctor);
        response.put("message", "Doctor added successfully");
        return ResponseEntity.ok(response);
    }

    /** PUT update doctor — admin only */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor,
            @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        if (!tokenService.validateAdminToken(token)) {
            response.put("error", "Forbidden: Admins only");
            return ResponseEntity.status(403).body(response);
        }
        doctor.setDoctorId(id);
        doctorService.saveDoctor(doctor);
        response.put("message", "Doctor updated successfully");
        return ResponseEntity.ok(response);
    }

    /** DELETE doctor — admin only */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        if (!tokenService.validateAdminToken(token)) {
            response.put("error", "Forbidden: Admins only");
            return ResponseEntity.status(403).body(response);
        }
        doctorService.deleteDoctor(id);
        response.put("message", "Doctor deleted successfully");
        return ResponseEntity.ok(response);
    }
}
