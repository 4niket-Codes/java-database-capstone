package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired private DoctorRepository doctorRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<Doctor> getAllDoctors() { return doctorRepository.findAll(); }

    public Optional<Doctor> getDoctorById(Long id) { return doctorRepository.findById(id); }

    public Optional<Doctor> getDoctorByUsername(String username) {
        return doctorRepository.findByUsername(username);
    }

    public List<Doctor> getDoctorsBySpecialityAndTime(String speciality, String time) {
        return doctorRepository.findBySpecializationOrderByExperience(speciality);
    }

    /**
     * Retrieves available time slots for a doctor on a specific date.
     * Filters the doctor's availableTimes list by matching the day of the week.
     * The role parameter can be used to restrict access (e.g., only PATIENT or DOCTOR roles).
     */
    public List<String> getAvailableTimeSlots(Long doctorId, String date, String role) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return new ArrayList<>();
        Doctor doctor = doctorOpt.get();
        LocalDate localDate = LocalDate.parse(date);
        String dayName = localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        List<String> slots = new ArrayList<>();
        if (doctor.getAvailableTimes() != null) {
            for (String slot : doctor.getAvailableTimes()) {
                if (slot.toLowerCase().contains(dayName.toLowerCase())) {
                    slots.add(slot);
                }
            }
        }
        return slots;
    }

    /**
     * Validates doctor login credentials.
     * Returns true if the username exists and the password matches the stored hash.
     */
    public boolean validateDoctorLogin(String username, String password) {
        return doctorRepository.findByUsername(username)
                .map(d -> passwordEncoder.matches(password, d.getPasswordHash()))
                .orElse(false);
    }

    public Doctor saveDoctor(Doctor doctor) {
        if (doctor.getDoctorId() == null && doctor.getPasswordHash() != null) {
            doctor.setPasswordHash(passwordEncoder.encode(doctor.getPasswordHash()));
        }
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) { doctorRepository.deleteById(id); }

    public boolean authenticateDoctor(String username, String password) {
        return validateDoctorLogin(username, password);
    }
}
