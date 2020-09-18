package com.keepit.keepitapi.repositories.admin;

import com.keepit.keepitapi.entities.admin.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
    Optional<List<Specialite>> findAllByArchive(boolean archive);
}
