package com.project.back_end.services;

import com.project.back_end.dto.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repositories.AppointmentRepository;
import com.project.back_end.repositories.DoctorRepository;
import com.project.back_end.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(int id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        return appointmentRepository.findByDoctor_DoctorId(doctorId);
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId);
    }

    public List<Appointment> getDailyAppointmentsByDoctor(int doctorId, LocalDate date) {
        return appointmentRepository.findDailyAppointmentsByDoctor(doctorId, date);
    }

    public Appointment createAppointment(AppointmentDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = new Appointment(
                patient, doctor,
                dto.getAppointmentDate(),
                dto.getAppointmentTime(),
                dto.getReasonForVisit()
        );
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointmentStatus(int id, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(int id) {
        appointmentRepository.deleteById(id);
    }

    public List<Object[]> getDoctorWithMostPatientsByMonth(int month, int year) {
        return appointmentRepository.findDoctorWithMostPatientsByMonth(month, year);
    }
}
