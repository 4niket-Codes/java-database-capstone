# Schema Design – Smart Clinic Management System

## Database: MySQL (Relational Data)

### Table: `admin`
| Column         | Type           | Constraints                        |
|----------------|----------------|------------------------------------|
| admin_id       | INT            | PRIMARY KEY, AUTO_INCREMENT        |
| username       | VARCHAR(50)    | UNIQUE, NOT NULL                   |
| password_hash  | VARCHAR(255)   | NOT NULL                           |
| email          | VARCHAR(100)   | UNIQUE, NOT NULL                   |
| full_name      | VARCHAR(100)   | NOT NULL                           |
| created_at     | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP          |

---

### Table: `doctor`
| Column              | Type           | Constraints                        |
|---------------------|----------------|------------------------------------|
| doctor_id           | INT            | PRIMARY KEY, AUTO_INCREMENT        |
| username            | VARCHAR(50)    | UNIQUE, NOT NULL                   |
| password_hash       | VARCHAR(255)   | NOT NULL                           |
| email               | VARCHAR(100)   | UNIQUE, NOT NULL                   |
| full_name           | VARCHAR(100)   | NOT NULL                           |
| specialization      | VARCHAR(100)   |                                    |
| license_number      | VARCHAR(50)    | UNIQUE                             |
| phone_number        | VARCHAR(20)    |                                    |
| years_of_experience | INT            |                                    |
| created_at          | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP          |

---

### Table: `patient`
| Column            | Type                            | Constraints                        |
|-------------------|---------------------------------|------------------------------------|
| patient_id        | INT                             | PRIMARY KEY, AUTO_INCREMENT        |
| username          | VARCHAR(50)                     | UNIQUE, NOT NULL                   |
| password_hash     | VARCHAR(255)                    | NOT NULL                           |
| email             | VARCHAR(100)                    | UNIQUE, NOT NULL                   |
| full_name         | VARCHAR(100)                    | NOT NULL                           |
| date_of_birth     | DATE                            |                                    |
| gender            | ENUM('Male','Female','Other')   |                                    |
| phone_number      | VARCHAR(20)                     |                                    |
| emergency_contact | VARCHAR(20)                     |                                    |
| blood_type        | VARCHAR(5)                      |                                    |
| created_at        | TIMESTAMP                       | DEFAULT CURRENT_TIMESTAMP          |

---

### Table: `appointment`
| Column             | Type                                                     | Constraints                              |
|--------------------|----------------------------------------------------------|------------------------------------------|
| appointment_id     | INT                                                      | PRIMARY KEY, AUTO_INCREMENT              |
| patient_id         | INT                                                      | NOT NULL, FK → patient(patient_id)       |
| doctor_id          | INT                                                      | NOT NULL, FK → doctor(doctor_id)         |
| appointment_date   | DATE                                                     | NOT NULL                                 |
| appointment_time   | TIME                                                     | NOT NULL                                 |
| status             | ENUM('Scheduled','Completed','Cancelled','No-show')      | DEFAULT 'Scheduled'                      |
| reason_for_visit   | TEXT                                                     |                                          |
| notes              | TEXT                                                     |                                          |
| created_at         | TIMESTAMP                                                | DEFAULT CURRENT_TIMESTAMP                |

---

### Table: `doctor_available_times`
| Column          | Type                                                                          | Constraints                        |
|-----------------|-------------------------------------------------------------------------------|------------------------------------|
| availability_id | INT                                                                           | PRIMARY KEY, AUTO_INCREMENT        |
| doctor_id       | INT                                                                           | NOT NULL, FK → doctor(doctor_id)   |
| day_of_week     | ENUM('Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday')  |                                    |
| start_time      | TIME                                                                          | NOT NULL                           |
| end_time        | TIME                                                                          | NOT NULL                           |

---

## Database: MongoDB (NoSQL – Prescriptions)

### Collection: `prescriptions`

```json
{
  "patient_id": 1,
  "doctor_id": 2,
  "appointment_id": 10,
  "medications": [
    {
      "name": "Amoxicillin",
      "dosage": "500mg",
      "frequency": "3 times a day",
      "duration": "7 days",
      "instructions": "Take with food"
    }
  ],
  "issue_date": "2024-03-14",
  "expiry_date": "2024-04-14",
  "refills_remaining": 2,
  "pharmacy_notes": "Generic substitution allowed"
}
```

---

## ER Diagram (Text Representation)

```
admin
  └── (manages system independently)

doctor ──< doctor_available_times
  │
  └──< appointment >──── patient
```

## Relationships

- One **Doctor** can have many **Appointments**.
- One **Patient** can have many **Appointments**.
- One **Doctor** can have many **Available Times**.
- One **Appointment** can have one **Prescription** (stored in MongoDB).
