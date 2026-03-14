# Database Schema

## Doctors
doctor_id (PK)
name
specialty
email

## Patients
patient_id (PK)
name
email
phone

## Appointments
appointment_id (PK)
doctor_id (FK)
patient_id (FK)
appointment_time

## Prescriptions
prescription_id (PK)
appointment_id (FK)
medicine
dosage
