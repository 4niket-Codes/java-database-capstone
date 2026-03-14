package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank
    private String username;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String passwordHash;

    @Column(unique = true, nullable = false, length = 100)
    @Email @NotBlank
    private String email;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String fullName;

    @Column(length = 100)
    private String specialization;

    @Column(unique = true, length = 50)
    private String licenseNumber;

    @Column(length = 20)
    private String phoneNumber;

    private int yearsOfExperience;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Available time slots stored as a collection of strings (e.g. "Monday 09:00-17:00")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "doctor_available_times", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "available_time")
    private List<String> availableTimes;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public Doctor() {}

    public Doctor(String username, String passwordHash, String email, String fullName,
                  String specialization, String licenseNumber, String phoneNumber, int yearsOfExperience) {
        this.username = username; this.passwordHash = passwordHash; this.email = email;
        this.fullName = fullName; this.specialization = specialization;
        this.licenseNumber = licenseNumber; this.phoneNumber = phoneNumber;
        this.yearsOfExperience = yearsOfExperience;
    }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int v) { this.yearsOfExperience = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<String> getAvailableTimes() { return availableTimes; }
    public void setAvailableTimes(List<String> availableTimes) { this.availableTimes = availableTimes; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
}
