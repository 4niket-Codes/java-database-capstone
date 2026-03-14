package com.project.back_end.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "prescriptions")
public class Prescription {

    @Id
    private String id;

    @Field("patient_id")
    private int patientId;

    @Field("doctor_id")
    private int doctorId;

    @Field("appointment_id")
    private int appointmentId;

    private List<Medication> medications;

    @Field("issue_date")
    private LocalDate issueDate;

    @Field("expiry_date")
    private LocalDate expiryDate;

    @Field("refills_remaining")
    private int refillsRemaining;

    @Field("pharmacy_notes")
    private String pharmacyNotes;

    // Inner class for Medication
    public static class Medication {
        private String name;
        private String dosage;
        private String frequency;
        private String duration;
        private String instructions;

        public Medication() {}

        public Medication(String name, String dosage, String frequency, String duration, String instructions) {
            this.name = name;
            this.dosage = dosage;
            this.frequency = frequency;
            this.duration = duration;
            this.instructions = instructions;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }

        public String getFrequency() { return frequency; }
        public void setFrequency(String frequency) { this.frequency = frequency; }

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }

        public String getInstructions() { return instructions; }
        public void setInstructions(String instructions) { this.instructions = instructions; }
    }

    // Constructors
    public Prescription() {}

    public Prescription(int patientId, int doctorId, int appointmentId,
                        List<Medication> medications, LocalDate issueDate,
                        LocalDate expiryDate, int refillsRemaining, String pharmacyNotes) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.medications = medications;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.refillsRemaining = refillsRemaining;
        this.pharmacyNotes = pharmacyNotes;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public List<Medication> getMedications() { return medications; }
    public void setMedications(List<Medication> medications) { this.medications = medications; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public int getRefillsRemaining() { return refillsRemaining; }
    public void setRefillsRemaining(int refillsRemaining) { this.refillsRemaining = refillsRemaining; }

    public String getPharmacyNotes() { return pharmacyNotes; }
    public void setPharmacyNotes(String pharmacyNotes) { this.pharmacyNotes = pharmacyNotes; }
}
