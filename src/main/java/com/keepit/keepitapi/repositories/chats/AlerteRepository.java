package com.keepit.keepitapi.repositories.chats;

import com.keepit.keepitapi.entities.chats.Alerte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Long> {
}
