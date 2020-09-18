package com.keepit.keepitapi.entities.gestionpatients;

import com.keepit.keepitapi.entities.chats.Alerte;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class HistoriqueConstante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp date;
    private Double valeur;

    @ManyToOne
    @JoinColumn(name = "constante", referencedColumnName = "id")
    private Constante constante;

    @ManyToOne
    @JoinColumn(name = "suivi", referencedColumnName = "id")
    private Suivi suivi;

    @ManyToOne
    @JoinColumn(name = "alerte", referencedColumnName = "id")
    private Alerte alerte;
}
