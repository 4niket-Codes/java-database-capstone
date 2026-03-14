-- ============================================================
-- Smart Clinic Management System - Complete Database Schema
-- cms.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS cms;
USE cms;

-- ============================================================
-- TABLE: admin
-- ============================================================
CREATE TABLE IF NOT EXISTS admin (
    admin_id       INT PRIMARY KEY AUTO_INCREMENT,
    username       VARCHAR(50)  UNIQUE NOT NULL,
    password_hash  VARCHAR(255) NOT NULL,
    email          VARCHAR(100) UNIQUE NOT NULL,
    full_name      VARCHAR(100) NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLE: doctor
-- ============================================================
CREATE TABLE IF NOT EXISTS doctor (
    doctor_id            INT PRIMARY KEY AUTO_INCREMENT,
    username             VARCHAR(50)  UNIQUE NOT NULL,
    password_hash        VARCHAR(255) NOT NULL,
    email                VARCHAR(100) UNIQUE NOT NULL,
    full_name            VARCHAR(100) NOT NULL,
    specialization       VARCHAR(100),
    license_number       VARCHAR(50)  UNIQUE,
    phone_number         VARCHAR(20),
    years_of_experience  INT DEFAULT 0,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLE: patient
-- ============================================================
CREATE TABLE IF NOT EXISTS patient (
    patient_id         INT PRIMARY KEY AUTO_INCREMENT,
    username           VARCHAR(50)  UNIQUE NOT NULL,
    password_hash      VARCHAR(255) NOT NULL,
    email              VARCHAR(100) UNIQUE NOT NULL,
    full_name          VARCHAR(100) NOT NULL,
    date_of_birth      DATE,
    gender             ENUM('Male','Female','Other'),
    phone_number       VARCHAR(20),
    emergency_contact  VARCHAR(20),
    blood_type         VARCHAR(5),
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLE: appointment
-- ============================================================
CREATE TABLE IF NOT EXISTS appointment (
    appointment_id    INT PRIMARY KEY AUTO_INCREMENT,
    patient_id        INT NOT NULL,
    doctor_id         INT NOT NULL,
    appointment_date  DATE NOT NULL,
    appointment_time  TIME NOT NULL,
    status            ENUM('Scheduled','Completed','Cancelled','NoShow') DEFAULT 'Scheduled',
    reason_for_visit  TEXT,
    notes             TEXT,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id)  REFERENCES doctor(doctor_id)  ON DELETE CASCADE
);

-- ============================================================
-- TABLE: doctor_available_times
-- ============================================================
CREATE TABLE IF NOT EXISTS doctor_available_times (
    availability_id  INT PRIMARY KEY AUTO_INCREMENT,
    doctor_id        INT NOT NULL,
    day_of_week      ENUM('Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'),
    start_time       TIME NOT NULL,
    end_time         TIME NOT NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id) ON DELETE CASCADE
);

-- ============================================================
-- INDEXES
-- ============================================================
CREATE INDEX idx_appointment_doctor_date   ON appointment(doctor_id, appointment_date);
CREATE INDEX idx_appointment_patient_status ON appointment(patient_id, status, appointment_date);
CREATE INDEX idx_doctor_specialization      ON doctor(specialization);

-- ============================================================
-- SEED DATA: admin
-- ============================================================
INSERT INTO admin (username, password_hash, email, full_name) VALUES
('admin',  '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'admin@clinic.com',  'System Administrator'),
('admin2', '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'admin2@clinic.com', 'Secondary Admin');

-- ============================================================
-- SEED DATA: doctor
-- ============================================================
INSERT INTO doctor (username, password_hash, email, full_name, specialization, license_number, phone_number, years_of_experience) VALUES
('dr_smith',   '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'smith@clinic.com',   'Dr. John Smith',     'Cardiology',      'LIC-001', '555-1001', 15),
('dr_jones',   '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'jones@clinic.com',   'Dr. Sarah Jones',    'Neurology',       'LIC-002', '555-1002', 12),
('dr_patel',   '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'patel@clinic.com',   'Dr. Raj Patel',      'Orthopedics',     'LIC-003', '555-1003', 10),
('dr_chen',    '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'chen@clinic.com',    'Dr. Li Chen',        'Dermatology',     'LIC-004', '555-1004', 8),
('dr_garcia',  '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'garcia@clinic.com',  'Dr. Maria Garcia',   'Pediatrics',      'LIC-005', '555-1005', 20),
('dr_wilson',  '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'wilson@clinic.com',  'Dr. Robert Wilson',  'Psychiatry',      'LIC-006', '555-1006', 18),
('dr_lee',     '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'lee@clinic.com',     'Dr. Amy Lee',        'Cardiology',      'LIC-007', '555-1007', 7),
('dr_kumar',   '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'kumar@clinic.com',   'Dr. Anil Kumar',     'Ophthalmology',   'LIC-008', '555-1008', 11),
('dr_brown',   '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'brown@clinic.com',   'Dr. James Brown',    'General Practice','LIC-009', '555-1009', 5),
('dr_taylor',  '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'taylor@clinic.com',  'Dr. Emily Taylor',   'Endocrinology',   'LIC-010', '555-1010', 9);

-- ============================================================
-- SEED DATA: patient
-- ============================================================
INSERT INTO patient (username, password_hash, email, full_name, date_of_birth, gender, phone_number, emergency_contact, blood_type) VALUES
('alice',    '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'alice@email.com',    'Alice Johnson',    '1990-05-14', 'Female', '555-2001', '555-3001', 'A+'),
('bob',      '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'bob@email.com',      'Bob Martinez',     '1985-08-22', 'Male',   '555-2002', '555-3002', 'O-'),
('charlie',  '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'charlie@email.com',  'Charlie Singh',    '1992-11-30', 'Male',   '555-2003', '555-3003', 'B+'),
('diana',    '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'diana@email.com',    'Diana Park',       '1978-03-17', 'Female', '555-2004', '555-3004', 'AB+'),
('evan',     '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'evan@email.com',     'Evan Williams',    '2000-07-04', 'Male',   '555-2005', '555-3005', 'O+'),
('fiona',    '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'fiona@email.com',    'Fiona Clark',      '1995-01-19', 'Female', '555-2006', '555-3006', 'A-'),
('george',   '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'george@email.com',   'George Davis',     '1988-09-25', 'Male',   '555-2007', '555-3007', 'B-'),
('helen',    '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'helen@email.com',    'Helen Thompson',   '1972-12-08', 'Female', '555-2008', '555-3008', 'AB-'),
('ivan',     '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'ivan@email.com',     'Ivan Petrov',      '1998-04-11', 'Male',   '555-2009', '555-3009', 'A+'),
('julia',    '$2a$10$xN6vRbPQ1N.zVnVkrFqSZ.ZJpHxDHBzTf5mnV7m9KtqMz.9kh3vSW', 'julia@email.com',    'Julia Roberts',    '1983-06-28', 'Female', '555-2010', '555-3010', 'O+');

-- ============================================================
-- SEED DATA: doctor_available_times
-- ============================================================
INSERT INTO doctor_available_times (doctor_id, day_of_week, start_time, end_time) VALUES
(1, 'Monday',    '09:00:00', '17:00:00'),
(1, 'Wednesday', '09:00:00', '17:00:00'),
(1, 'Friday',    '09:00:00', '13:00:00'),
(2, 'Tuesday',   '10:00:00', '18:00:00'),
(2, 'Thursday',  '10:00:00', '18:00:00'),
(3, 'Monday',    '08:00:00', '16:00:00'),
(3, 'Tuesday',   '08:00:00', '16:00:00'),
(3, 'Thursday',  '08:00:00', '16:00:00'),
(4, 'Wednesday', '11:00:00', '19:00:00'),
(4, 'Friday',    '11:00:00', '19:00:00'),
(5, 'Monday',    '07:00:00', '15:00:00'),
(5, 'Tuesday',   '07:00:00', '15:00:00'),
(5, 'Wednesday', '07:00:00', '15:00:00');

-- ============================================================
-- SEED DATA: appointment
-- ============================================================
INSERT INTO appointment (patient_id, doctor_id, appointment_date, appointment_time, status, reason_for_visit) VALUES
(1,  1, '2024-03-01', '09:00:00', 'Completed',  'Chest pain evaluation'),
(2,  1, '2024-03-01', '10:00:00', 'Completed',  'Routine cardiac checkup'),
(3,  2, '2024-03-01', '10:00:00', 'Completed',  'Severe headaches'),
(4,  3, '2024-03-02', '08:30:00', 'Completed',  'Knee pain'),
(5,  1, '2024-03-04', '09:00:00', 'Completed',  'Follow-up ECG'),
(6,  5, '2024-03-04', '07:00:00', 'Completed',  'Child vaccination'),
(7,  2, '2024-03-05', '11:00:00', 'Cancelled',  'Dizziness'),
(8,  4, '2024-03-06', '12:00:00', 'Completed',  'Skin rash consultation'),
(9,  1, '2024-03-06', '14:00:00', 'Scheduled',  'Heart palpitations'),
(10, 3, '2024-03-07', '09:00:00', 'Scheduled',  'Back pain'),
(1,  5, '2024-03-08', '08:00:00', 'Scheduled',  'Annual checkup'),
(2,  2, '2024-03-08', '10:00:00', 'Scheduled',  'Migraine follow-up'),
(3,  1, '2024-03-11', '09:00:00', 'Scheduled',  'Blood pressure check'),
(4,  4, '2024-03-11', '13:00:00', 'Scheduled',  'Eczema treatment'),
(5,  3, '2024-03-12', '08:30:00', 'Scheduled',  'Sports injury');

-- ============================================================
-- STORED PROCEDURE: GetDailyAppointmentReportByDoctor
-- ============================================================
DELIMITER $$

CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN p_doctor_id   INT,
    IN p_report_date DATE
)
BEGIN
    SELECT
        a.appointment_id,
        p.full_name          AS patient_name,
        p.phone_number       AS patient_phone,
        a.appointment_time,
        a.status,
        a.reason_for_visit
    FROM appointment a
    JOIN patient p ON a.patient_id = p.patient_id
    WHERE a.doctor_id    = p_doctor_id
      AND a.appointment_date = p_report_date
    ORDER BY a.appointment_time ASC;
END$$

DELIMITER ;

-- ============================================================
-- STORED PROCEDURE: GetDoctorWithMostPatientsByMonth
-- ============================================================
DELIMITER $$

CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(
    IN p_month INT,
    IN p_year  INT
)
BEGIN
    SELECT
        d.doctor_id,
        d.full_name                          AS doctor_name,
        d.specialization,
        COUNT(DISTINCT a.patient_id)         AS unique_patients,
        COUNT(a.appointment_id)              AS total_appointments
    FROM doctor d
    LEFT JOIN appointment a
           ON d.doctor_id = a.doctor_id
          AND MONTH(a.appointment_date) = p_month
          AND YEAR(a.appointment_date)  = p_year
    GROUP BY d.doctor_id, d.full_name, d.specialization
    ORDER BY unique_patients DESC, total_appointments DESC
    LIMIT 5;
END$$

DELIMITER ;

-- ============================================================
-- STORED PROCEDURE: GetDoctorWithMostPatientsByYear
-- ============================================================
DELIMITER $$

CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
    IN p_year INT
)
BEGIN
    SELECT
        d.doctor_id,
        d.full_name                          AS doctor_name,
        d.specialization,
        COUNT(DISTINCT a.patient_id)         AS unique_patients,
        COUNT(a.appointment_id)              AS total_appointments
    FROM doctor d
    LEFT JOIN appointment a
           ON d.doctor_id = a.doctor_id
          AND YEAR(a.appointment_date) = p_year
    GROUP BY d.doctor_id, d.full_name, d.specialization
    ORDER BY unique_patients DESC, total_appointments DESC
    LIMIT 5;
END$$

DELIMITER ;
