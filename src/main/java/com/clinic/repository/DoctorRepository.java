package com.project.back_end.repositories;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Optional<Doctor> findByUsername(String username);

    Optional<Doctor> findByEmail(String email);

    List<Doctor> findBySpecialization(String specialization);

    List<Doctor> findByFullNameContainingIgnoreCase(String name);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT d FROM Doctor d WHERE d.specialization = :spec ORDER BY d.yearsOfExperience DESC")
    List<Doctor> findBySpecializationOrderByExperience(@Param("spec") String specialization);
}
