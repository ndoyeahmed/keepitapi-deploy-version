package com.keepit.keepitapi.services.admin;

import com.keepit.keepitapi.entities.admin.Medecin;
import com.keepit.keepitapi.entities.admin.Patient;
import com.keepit.keepitapi.entities.admin.Specialite;
import com.keepit.keepitapi.repositories.admin.MedecinRepository;
import com.keepit.keepitapi.repositories.admin.PatientRepository;
import com.keepit.keepitapi.repositories.admin.SpecialiteRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log
@Transactional
public class MedecinPatientService {

    private MedecinRepository medecinRepository;
    private PatientRepository patientRepository;
    private SpecialiteRepository specialiteRepository;

    @Autowired
    public MedecinPatientService(MedecinRepository medecinRepository, PatientRepository patientRepository,
                                 SpecialiteRepository specialiteRepository) {
        this.medecinRepository = medecinRepository;
        this.patientRepository = patientRepository;
        this.specialiteRepository = specialiteRepository;
    }

    /**
     * add new Medecin
     * @param medecin to add
     * @return the new medecin added
     */
    public Medecin addMedecin(Medecin medecin) {
        try {
            medecinRepository.save(medecin);
            return medecin;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * find medecin by id
     * @param id search reference
     * @return return a medecin if found one or return null if not
     */
    public Medecin findMedecinById(Long id) {
        return medecinRepository.findById(id).orElse(null);
    }

    /**
     * get all medecin
     * @param archive
     * @param statut
     * @return list of medecin
     */
    public List<Medecin> findAllMedecin(boolean archive, boolean statut) {
        return medecinRepository.findAllByArchiveAndStatus(archive, statut)
                .orElse(new ArrayList<>());
    }

    /**
     * add new patient
     * @param patient to add
     * @return the new patient added
     */
    public Patient addPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return patient;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * find a patient by an id
     * @param id search reference
     * @return return a patient if found one or return null if not
     */
    public Patient findPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    /**
     * get all patient
     * @param archive
     * @param statut
     * @return list of patient
     */
    public List<Patient> findAllPatient(boolean archive, boolean statut) {
        return patientRepository.findAllByArchiveAndStatus(archive, statut)
                .orElse(new ArrayList<>());
    }

    /**
     * add new Specialite
     * @param specialite to add
     * @return the new specialite added
     */
    public Specialite addSpecialite(Specialite specialite) {
        try {
            specialiteRepository.save(specialite);
            return specialite;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * find specialite by id
     * @param id search reference
     * @return return a specialite if found one or return null if not
     */
    public Specialite findSpecialiteById(Long id) {
        return specialiteRepository.findById(id).orElse(null);
    }

    /**
     * get all specialite
     * @return list of specialite
     */
    public List<Specialite> findAllSpecialite(boolean archive) {
        return specialiteRepository.findAllByArchive(archive)
                .orElse(new ArrayList<>());
    }
}
