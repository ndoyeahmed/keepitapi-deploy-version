package com.keepit.keepitapi.repositories.admin;

import com.keepit.keepitapi.entities.admin.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    Optional<List<Medecin>> findAllByArchiveAndStatus(boolean archive, boolean statut);
}
