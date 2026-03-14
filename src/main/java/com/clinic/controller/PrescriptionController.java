package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private TokenService tokenService;

    // GET prescriptions by patient ID
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByPatient(
            @PathVariable int patientId,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatient(patientId));
    }

    // GET prescriptions by doctor ID
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Prescription>> getPrescriptionsByDoctor(
            @PathVariable int doctorId,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByDoctor(doctorId));
    }

    // POST create a new prescription (doctor only)
    @PostMapping
    public ResponseEntity<Prescription> createPrescription(
            @RequestBody Prescription prescription,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateDoctorToken(token)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(prescriptionService.savePrescription(prescription));
    }

    // PUT update prescription (doctor only)
    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(
            @PathVariable String id,
            @RequestBody Prescription prescription,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateDoctorToken(token)) {
            return ResponseEntity.status(403).build();
        }
        prescription.setId(id);
        return ResponseEntity.ok(prescriptionService.savePrescription(prescription));
    }

    // DELETE prescription (doctor only)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePrescription(
            @PathVariable String id,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateDoctorToken(token)) {
            return ResponseEntity.status(403).body("Forbidden: Doctors only");
        }
        prescriptionService.deletePrescription(id);
        return ResponseEntity.ok("Prescription deleted successfully");
    }
}
