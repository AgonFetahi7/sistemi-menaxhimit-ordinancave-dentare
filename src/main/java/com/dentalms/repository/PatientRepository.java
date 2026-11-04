package com.dentalms.repository;

import com.dentalms.model.Patient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String first, String last);
}
