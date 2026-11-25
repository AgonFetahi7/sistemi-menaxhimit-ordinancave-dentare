package com.dentalms.repository;

import com.dentalms.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistRepository extends JpaRepository<Dentist, Long> {
}
