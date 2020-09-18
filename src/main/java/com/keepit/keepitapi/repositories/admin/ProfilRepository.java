package com.keepit.keepitapi.repositories.admin;

import com.keepit.keepitapi.entities.admin.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, Long> {
    Optional<Profil> findByLibelle(String libelle);
}
