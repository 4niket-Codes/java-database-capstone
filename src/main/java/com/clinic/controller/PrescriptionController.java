package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired private PrescriptionService prescriptionService;
    @Autowired private TokenService tokenService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(
            @PathVariable int patientId,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatient(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByDoctor(
            @PathVariable int doctorId,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByDoctor(doctorId));
    }

    /** POST create prescription — @Valid validates input; returns structured Map response */
    @PostMapping
    public ResponseEntity<Map<String, String>> createPrescription(
            @Valid @RequestBody Prescription prescription,
            @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        if (!tokenService.validateDoctorToken(token)) {
            response.put("error", "Forbidden: Doctors only");
            return ResponseEntity.status(403).body(response);
        }
        Prescription saved = prescriptionService.savePrescription(prescription);
        response.put("message", "Prescription created successfully");
        response.put("id", saved.getId());
        return ResponseEntity.ok(response);
    }

    /** PUT update prescription — @Valid validates input; returns structured Map response */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updatePrescription(
            @PathVariable String id,
            @Valid @RequestBody Prescription prescription,
            @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        if (!tokenService.validateDoctorToken(token)) {
            response.put("error", "Forbidden: Doctors only");
            return ResponseEntity.status(403).body(response);
        }
        prescription.setId(id);
        prescriptionService.savePrescription(prescription);
        response.put("message", "Prescription updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePrescription(
            @PathVariable String id,
            @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        if (!tokenService.validateDoctorToken(token)) {
            response.put("error", "Forbidden: Doctors only");
            return ResponseEntity.status(403).body(response);
        }
        prescriptionService.deletePrescription(id);
        response.put("message", "Prescription deleted successfully");
        return ResponseEntity.ok(response);
    }
}
