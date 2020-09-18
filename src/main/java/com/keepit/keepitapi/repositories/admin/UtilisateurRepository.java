package com.keepit.keepitapi.repositories.admin;

import com.keepit.keepitapi.entities.admin.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    @Query("select u from Utilisateur u where u.login=:login and u.status=:status")
    Optional<Utilisateur> connexion(@Param("login") String login, @Param("status") Boolean status);

    Optional<List<Utilisateur>> findAllByArchive(boolean archive);

    Optional<Utilisateur> findByIdAndArchiveFalseAndStatusTrue(Long id);

    Optional<List<Utilisateur>> findAllByArchiveAndStatus(boolean archive, boolean statut);
}
