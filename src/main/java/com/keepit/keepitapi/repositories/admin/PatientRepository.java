package com.keepit.keepitapi.repositories.admin;

import com.keepit.keepitapi.entities.admin.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<List<Patient>> findAllByArchiveAndStatus(boolean archive, boolean statut);
}
