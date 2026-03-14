package com.project.back_end.repositories;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUsername(String username);

    Optional<Patient> findByEmail(String email);

    /** Retrieve a patient by either their email address or phone number */
    Optional<Patient> findByEmailOrPhoneNumber(String email, String phoneNumber);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
