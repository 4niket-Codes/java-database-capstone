package com.project.back_end.controllers;

import com.project.back_end.dto.LoginRequest;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.repositories.AdminRepository;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private TokenService tokenService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // POST admin login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Optional<Admin> admin = adminRepository.findByUsername(request.getUsername());
        Map<String, String> response = new HashMap<>();

        if (admin.isPresent() && passwordEncoder.matches(request.getPassword(), admin.get().getPasswordHash())) {
            String token = tokenService.generateToken(request.getUsername(), "ADMIN");
            response.put("token", token);
            response.put("role", "ADMIN");
            response.put("name", admin.get().getFullName());
            return ResponseEntity.ok(response);
        }
        response.put("error", "Invalid credentials");
        return ResponseEntity.status(401).body(response);
    }

    // GET all doctors (admin)
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors(
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateAdminToken(token)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // POST add doctor (admin)
    @PostMapping("/doctors")
    public ResponseEntity<String> addDoctor(
            @RequestBody Doctor doctor,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateAdminToken(token)) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        doctorService.saveDoctor(doctor);
        return ResponseEntity.ok("Doctor added successfully");
    }

    // DELETE doctor (admin)
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<String> deleteDoctor(
            @PathVariable int id,
            @RequestHeader("Authorization") String token) {
        if (!tokenService.validateAdminToken(token)) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor deleted successfully");
    }
}
