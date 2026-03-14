package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(int id) {
        return doctorRepository.findById(id);
    }

    public Optional<Doctor> getDoctorByUsername(String username) {
        return doctorRepository.findByUsername(username);
    }

    public List<Doctor> getDoctorsBySpecialityAndTime(String speciality, String time) {
        // Filter by specialization; time filtering can be added via join with DoctorAvailableTimes
        return doctorRepository.findBySpecializationOrderByExperience(speciality);
    }

    public Doctor saveDoctor(Doctor doctor) {
        if (doctor.getDoctorId() == 0 && doctor.getPasswordHash() != null) {
            // Hash password for new doctors
            doctor.setPasswordHash(passwordEncoder.encode(doctor.getPasswordHash()));
        }
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(int id) {
        doctorRepository.deleteById(id);
    }

    public boolean authenticateDoctor(String username, String password) {
        Optional<Doctor> doctor = doctorRepository.findByUsername(username);
        return doctor.isPresent() && passwordEncoder.matches(password, doctor.get().getPasswordHash());
    }
}
