package com.keepit.keepitapi.entities.admin;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keepit.keepitapi.entities.chats.Message;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@JsonFilter("passwordFilter")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"login", "email", "telephone"})
})
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String nom;
    protected String prenom;
    protected String login;
    protected String password;
    protected String email;
    protected String adresse;
    protected String telephone;
    @Lob
    protected String photo;
    @Column(columnDefinition = "boolean default false")
    protected boolean archive;
    @Column(columnDefinition = "boolean default false")
    protected boolean status;
    @Column(columnDefinition = "boolean default false")
    protected boolean passwordChange;

    @ManyToOne
    @JoinColumn(name = "profil", referencedColumnName = "id")
    protected Profil profil;

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    protected List<Message> messages;

    @JsonProperty("nomComplet")
    protected String getNomComplet() {
        return this.prenom + " " + this.nom;
    }
}
