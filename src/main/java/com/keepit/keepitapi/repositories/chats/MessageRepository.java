package com.keepit.keepitapi.repositories.chats;

import com.keepit.keepitapi.entities.chats.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where (m.utilisateur.email=:email or m.destinataire=:email) and m.date = (select sms.date from Message sms " +
            "where (sms.utilisateur.email=:email or sms.destinataire=:email))")
    Optional<List<Message>> findAllByUtilisateurAndDestinataire(@Param("email") String email);
}
