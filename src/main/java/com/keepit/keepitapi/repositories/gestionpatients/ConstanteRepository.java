package com.keepit.keepitapi.repositories.gestionpatients;

import com.keepit.keepitapi.entities.gestionpatients.Constante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConstanteRepository extends JpaRepository<Constante, Long> {
    Optional<List<Constante>> findAllByArchive(boolean archive);
}
