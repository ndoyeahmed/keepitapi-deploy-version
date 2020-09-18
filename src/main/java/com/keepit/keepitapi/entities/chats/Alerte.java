package com.keepit.keepitapi.entities.chats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.keepit.keepitapi.entities.gestionpatients.HistoriqueConstante;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class Alerte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp date;
    private Double longitude;
    private Double latitude;
    private String notification;
    private Integer level;
    @Column(columnDefinition = "boolean default false")
    private boolean processed;

    @OneToMany(mappedBy = "alerte")
    @JsonIgnore
    private List<HistoriqueConstante> historiqueConstantes;
}
