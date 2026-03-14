package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private TokenService tokenService;

    // GET all doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors(
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // GET doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(
            @PathVariable int id,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        return doctorService.getDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET doctors by specialization and available time
    @GetMapping("/search")
    public ResponseEntity<List<Doctor>> getDoctorsBySpecialityAndTime(
            @RequestParam String speciality,
            @RequestParam(required = false) String time,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateToken(token)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialityAndTime(speciality, time));
    }

    // POST add a new doctor (admin only)
    @PostMapping
    public ResponseEntity<String> addDoctor(
            @RequestBody Doctor doctor,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateAdminToken(token)) {
            return ResponseEntity.status(403).body("Forbidden: Admins only");
        }
        doctorService.saveDoctor(doctor);
        return ResponseEntity.ok("Doctor added successfully");
    }

    // PUT update doctor
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDoctor(
            @PathVariable int id,
            @RequestBody Doctor doctor,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateAdminToken(token)) {
            return ResponseEntity.status(403).body("Forbidden: Admins only");
        }
        doctor.setDoctorId(id);
        doctorService.saveDoctor(doctor);
        return ResponseEntity.ok("Doctor updated successfully");
    }

    // DELETE remove doctor (admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(
            @PathVariable int id,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateAdminToken(token)) {
            return ResponseEntity.status(403).body("Forbidden: Admins only");
        }
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor deleted successfully");
    }
}
