package com.keepit.keepitapi.repositories.gestionpatients;

import com.keepit.keepitapi.entities.admin.Medecin;
import com.keepit.keepitapi.entities.gestionpatients.HistoriqueConstante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriqueConstanteRepository extends JpaRepository<HistoriqueConstante, Long> {
    Optional<List<HistoriqueConstante>> findAllByAlerteNotNull();

    @Query("select h from HistoriqueConstante h where h.alerte is not null and h.alerte.processed=:processed and h.suivi.id in (select s.id from Suivi s where s.medecin=:medecin)")
    Optional<List<HistoriqueConstante>> findAllByAlerteAndConnectedMedecin(@Param("medecin") Medecin medecin, @Param("processed") boolean processed);
}
