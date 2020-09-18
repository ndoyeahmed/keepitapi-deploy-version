package com.keepit.keepitapi.entities.chats;

import com.keepit.keepitapi.entities.admin.Utilisateur;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sms;
    private Timestamp date;
    private String destinataire;
    private Integer type;

    @ManyToOne
    @JoinColumn(name = "utilisateur", referencedColumnName = "id")
    private Utilisateur utilisateur;
}
