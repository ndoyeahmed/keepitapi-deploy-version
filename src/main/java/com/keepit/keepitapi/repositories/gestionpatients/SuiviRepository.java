package com.keepit.keepitapi.repositories.gestionpatients;

import com.keepit.keepitapi.entities.admin.Medecin;
import com.keepit.keepitapi.entities.gestionpatients.Suivi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuiviRepository extends JpaRepository<Suivi, Long> {

    Optional<List<Suivi>> findAllByMedecin(Medecin medecin);
}
