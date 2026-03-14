package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repositories.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Optional<Prescription> getPrescriptionById(String id) {
        return prescriptionRepository.findById(id);
    }

    public List<Prescription> getPrescriptionsByPatient(int patientId) {
        return prescriptionRepository.findByPatientId(patientId);
    }

    public List<Prescription> getPrescriptionsByDoctor(int doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId);
    }

    public List<Prescription> getPrescriptionsByAppointment(int appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }

    public Prescription savePrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public void deletePrescription(String id) {
        prescriptionRepository.deleteById(id);
    }
}
