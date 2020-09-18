package com.keepit.keepitapi.services.gestionpatients;

import com.keepit.keepitapi.entities.admin.Medecin;
import com.keepit.keepitapi.entities.admin.Utilisateur;
import com.keepit.keepitapi.entities.gestionpatients.Constante;
import com.keepit.keepitapi.entities.gestionpatients.HistoriqueConstante;
import com.keepit.keepitapi.entities.gestionpatients.Suivi;
import com.keepit.keepitapi.repositories.gestionpatients.ConstanteRepository;
import com.keepit.keepitapi.repositories.gestionpatients.HistoriqueConstanteRepository;
import com.keepit.keepitapi.repositories.gestionpatients.SuiviRepository;
import com.keepit.keepitapi.services.admin.MedecinPatientService;
import com.keepit.keepitapi.services.admin.UtilisateurService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log
@Transactional
public class GestionPatientService {
    private ConstanteRepository constanteRepository;
    private HistoriqueConstanteRepository historiqueConstanteRepository;
    private SuiviRepository suiviRepository;
    private MedecinPatientService medecinPatientService;
    private UtilisateurService utilisateurService;

    // dependency injection by constructor
    @Autowired
    public GestionPatientService(ConstanteRepository constanteRepository, SuiviRepository suiviRepository,
                                 HistoriqueConstanteRepository historiqueConstanteRepository,
                                 MedecinPatientService medecinPatientService, UtilisateurService utilisateurService) {
        this.constanteRepository = constanteRepository;
        this.suiviRepository = suiviRepository;
        this.historiqueConstanteRepository = historiqueConstanteRepository;
        this.medecinPatientService = medecinPatientService;
        this.utilisateurService = utilisateurService;
    }

    /**
     * add a new Constante
     * @param constante to add
     * @return return the added constante or generate an exception
     */
    public Constante addConstante(Constante constante) {
        try {
            constanteRepository.save(constante);
            return constante;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * find a constant by an id
     * @param id the search reference
     * @return return a constante if found one or return null if not
     */
    public Constante findConstanteById(Long id) {
        return constanteRepository.findById(id)
                .orElse(null);
    }

    /**
     * find all constante filtered by the archive attribut
     * @param archive search reference
     * @return list of constante
     */
    public List<Constante> findAllConstante(boolean archive) {
        return constanteRepository.findAllByArchive(archive)
                .orElse(new ArrayList<>());
    }

    /**
     * add new Suivi
     * @param suivi to add
     * @return added element or throw a new exception
     */
    public Suivi addSuivi(Suivi suivi) {
        try {
            suivi.setDate(Timestamp.valueOf(LocalDateTime.now()));
            suiviRepository.save(suivi);
            return suivi;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * find a suivi object by an id
     * @param id the search reference
     * @return suivi if found one or null if not
     */
    public Suivi findSuiviById(Long id) {
        return suiviRepository.findById(id)
                .orElse(null);
    }

    /**
     * get all suivi
     * @return list of suivi
     */
    public List<Suivi> findAllSuivi() {
        return suiviRepository.findAll();
    }

    /**
     * add new {@link HistoriqueConstante}
     * @param historiqueConstante to add
     * @return added item or throw an exception
     */
    public HistoriqueConstante addHistoriqueConstante(HistoriqueConstante historiqueConstante) {
        try {
            historiqueConstanteRepository.save(historiqueConstante);
            return historiqueConstante;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * find {@link HistoriqueConstante}
     * @param id the search reference
     * @return HistoriqueConstante if found one or return null
     */
    public HistoriqueConstante findHistoriqueConstanteById(Long id) {
        return historiqueConstanteRepository.findById(id)
                .orElse(null);
    }

    /**
     * fina all {@link HistoriqueConstante}
     * @return list of {@link HistoriqueConstante}
     */
    public List<HistoriqueConstante> findAllHistoriqueConstante() {
        return historiqueConstanteRepository.findAll();
    }

    /**
     * find all {@link HistoriqueConstante} by alerte not null
     * @return List of {@link HistoriqueConstante}
     */
    public List<HistoriqueConstante> findAllHistoriqueConstanteByAlerteNotNull() {
        return historiqueConstanteRepository.findAllByAlerteNotNull().orElse(new ArrayList<>());
    }

    /**
     * get historique constante for the patients of the connected medecin
     * @return list historique constante
     */
    public List<HistoriqueConstante> findAllHistoriqueByAlerteAndConnectedMedecin(boolean processed) {
        Utilisateur utilisateur = utilisateurService.connectedUser();
        if (utilisateur.getProfil().getLibelle().equals("MEDECIN")) {
            Medecin medecin = medecinPatientService.findMedecinById(utilisateur.getId());
            if (medecin != null) {
                return historiqueConstanteRepository.findAllByAlerteAndConnectedMedecin(medecin, processed)
                        .orElse(new ArrayList<>());
            }
        }

        return new ArrayList<>();
    }

    /**
     * find all patient for a medecin
     * @return list of {@link Suivi}
     */
    public List<Suivi> findAllSuiviByMedecin() {
        Utilisateur utilisateur = utilisateurService.connectedUser();
        if (utilisateur.getProfil().getLibelle().equals("MEDECIN")) {
            Medecin medecin = medecinPatientService.findMedecinById(utilisateur.getId());
            if (medecin != null) {
                return suiviRepository.findAllByMedecin(medecin)
                        .orElse(new ArrayList<>());
            }
        }

        return new ArrayList<>();
    }
}
