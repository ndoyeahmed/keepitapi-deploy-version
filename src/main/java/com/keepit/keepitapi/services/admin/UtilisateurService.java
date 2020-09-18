package com.keepit.keepitapi.services.admin;

import com.keepit.keepitapi.entities.admin.Profil;
import com.keepit.keepitapi.entities.admin.Utilisateur;
import com.keepit.keepitapi.repositories.admin.ProfilRepository;
import com.keepit.keepitapi.repositories.admin.UtilisateurRepository;
import com.keepit.keepitapi.rest.exceptions.BadRequestException;
import com.keepit.keepitapi.rest.exceptions.EntityNotFoundException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Log
@Transactional
public class UtilisateurService {

    private UtilisateurRepository utilisateurRepository;
    private BCryptPasswordEncoder encoder;
    private ProfilRepository profilRepository;
    private MailService mailService;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              ProfilRepository profilRepository,
                              MailService mailService,
                              BCryptPasswordEncoder encoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.encoder = encoder;
        this.profilRepository = profilRepository;
        this.mailService = mailService;
    }

    public Utilisateur connectedUser() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            String login = "";
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails user = (UserDetails) authentication.getPrincipal();
                login = user.getUsername();
            }
            if (authentication.getPrincipal() instanceof String)
                login = (String) authentication.getPrincipal();
            return utilisateurRepository
                    .connexion(login, true)
                    .map(user -> user)
                    .orElse(null);
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    public List<Utilisateur> findAllByArchiveFalseAndStatutTrue(boolean archive) {
        return utilisateurRepository.findAllByArchive(archive).orElse(new ArrayList<>());
    }

    public List<Utilisateur> findAllUserByArchiveAndStatut(boolean archive, boolean statut) {
        return utilisateurRepository.findAllByArchiveAndStatus(archive, statut)
                .orElse(new ArrayList<>());
    }

    public List<Profil> findAllProfil() {
        return profilRepository.findAll();
    }

    public Profil findProfilByLibelle(String libelle) {
        return profilRepository.findByLibelle(libelle.toUpperCase()).orElse(null);
    }

    public Utilisateur findUserByIdAndArchiveFalseAndStatutTrue(Long id) {
        return utilisateurRepository.findByIdAndArchiveFalseAndStatusTrue(id).orElse(null);
    }

    public Utilisateur updateStatutUser(Map<String, String> body, Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user not found"));

        if (body.get("archive") != null) {
            utilisateur.setArchive(Boolean.parseBoolean(body.get("archive")));
        }
        if (body.get("statut") != null) {
            utilisateur.setStatus(Boolean.parseBoolean(body.get("statut")));
        }

        // update user status
        utilisateurRepository.save(utilisateur);

        return utilisateur;
    }

    public Utilisateur resetUserPassword(Map<String, String> body) {
        Utilisateur utilisateur = connectedUser();
        utilisateur.setPasswordChange(true);
        if (body.get("password").equals(body.get("confirmPassword"))) {
            utilisateur.setPassword(encoder.encode(body.get("password")));
        } else throw new BadRequestException("password not match");

        utilisateurRepository.save(utilisateur);
        return utilisateur;
    }

    public String encodePassword(String password) {
        return encoder.encode(password);
    }

    public Profil addDefaultProfil() {
        Profil profil = new Profil();
        profil.setLibelle("ADMIN");
        profil.setDescription("Administrateur plateforme");
        Profil profil1 = new Profil();
        profil1.setLibelle("MEDECIN");
        profil1.setDescription("Medecin specialiste");
        Profil profil2 = new Profil();
        profil2.setLibelle("PATIENT");
        profil2.setDescription("utilisateur app mobile");
        profilRepository.save(profil);
        profilRepository.save(profil1);
        profilRepository.save(profil2);

        return profil;
    }

    public void addDefaultAdmin() {
        List<Profil> profils = profilRepository.findAll();
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        Profil profil;
        if (profils.isEmpty()) {
            profil = addDefaultProfil();
        } else profil = profilRepository.findByLibelle("ADMIN").orElse(null);

        if (utilisateurs.isEmpty() && profil != null) {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setArchive(false);
            utilisateur.setPasswordChange(true);
            utilisateur.setStatus(true);
            utilisateur.setAdresse("Orange Digital Center");
            utilisateur.setEmail("admin@mail.com");
            utilisateur.setLogin("admin");
            utilisateur.setNom("admin");
            utilisateur.setPrenom("admin");
            utilisateur.setTelephone("770000000");
            utilisateur.setProfil(profil);
            utilisateur.setPhoto(null);
            utilisateur.setPassword(encoder.encode("passer"));

            utilisateurRepository.save(utilisateur);
        }
    }

}
