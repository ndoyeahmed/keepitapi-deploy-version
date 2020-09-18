package com.keepit.keepitapi.rest.controllers.admin;

import com.keepit.keepitapi.entities.admin.Medecin;
import com.keepit.keepitapi.entities.admin.Patient;
import com.keepit.keepitapi.entities.admin.Specialite;
import com.keepit.keepitapi.rest.exceptions.BadRequestException;
import com.keepit.keepitapi.rest.exceptions.EntityNotFoundException;
import com.keepit.keepitapi.rest.exceptions.InternalServerErrorException;
import com.keepit.keepitapi.services.admin.MedecinPatientService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log
@RequestMapping("/api/")
public class MedecinPatientController {

    private MedecinPatientService medecinPatientService;

    @Autowired
    public MedecinPatientController(MedecinPatientService medecinPatientService) {
        this.medecinPatientService = medecinPatientService;
    }

    @GetMapping("medecins/archive/{archive}/statut/{statut}")
    public ResponseEntity<?> getAllMedecin(@PathVariable Boolean archive, @PathVariable Boolean statut) {
        if (archive == null) throw new BadRequestException("archive cannot be null");
        if (statut == null) throw new BadRequestException("statut cannot be null");

        return ResponseEntity.ok(medecinPatientService.findAllMedecin(archive, statut));
    }

    @GetMapping("medecins/{id}")
    public ResponseEntity<?> getMedecinById(@PathVariable Long id) {
        if (id == null) throw new BadRequestException("id cannot be null");
        Medecin medecin = medecinPatientService.findMedecinById(id);
        if (medecin == null) throw new EntityNotFoundException("medecin not found");

        return ResponseEntity.ok(medecin);
    }

    @PostMapping("medecins")
    public ResponseEntity<?> addMedecin(@RequestBody Medecin medecin) {
        if (medecin == null) throw new BadRequestException("medecin cannot be null");

        verifyFieldUtilisateur(medecin.getTelephone(), medecin.getEmail(),
                medecin.getLogin(), medecin.getNom(), medecin.getPrenom());

        Medecin medecin1 = medecinPatientService.addMedecin(medecin);

        if (medecin1 == null) throw new InternalServerErrorException("duplicate entry");
        else return ResponseEntity.status(HttpStatus.CREATED)
                .body(medecin1);


    }

    @GetMapping("patients/archive/{archive}/statut/{statut}")
    public ResponseEntity<?> getAllPatient(@PathVariable Boolean archive, @PathVariable Boolean statut) {
        if (archive == null) throw new BadRequestException("archive cannot be null");
        if (statut == null) throw new BadRequestException("statut cannot be null");

        return ResponseEntity.ok(medecinPatientService.findAllPatient(archive, statut));
    }

    @GetMapping("patients/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        if (id == null) throw new BadRequestException("id cannot be null");
        Patient patient = medecinPatientService.findPatientById(id);
        if (patient == null) throw new EntityNotFoundException("patient not found");

        return ResponseEntity.ok(patient);
    }

    @PostMapping("patients")
    public ResponseEntity<?> addPatient(@RequestBody Patient patient) {
        if (patient == null) throw new BadRequestException("patient cannot be null");
        if (patient.getCin() == null || patient.getCin().trim().equals(""))
            throw new BadRequestException("cin required");
        if (patient.getDateNaissance() == null )
            throw new BadRequestException("date naissance required");

        verifyFieldUtilisateur(patient.getTelephone(), patient.getEmail(),
                patient.getLogin(), patient.getNom(), patient.getPrenom());

        Patient patient1 = medecinPatientService.addPatient(patient);

        if (patient1 == null) throw new InternalServerErrorException("duplicate entry");
        else return ResponseEntity.status(HttpStatus.CREATED)
                .body(patient1);


    }

    private void verifyFieldUtilisateur(String telephone, String email, String login, String nom, String prenom) {
        if (telephone == null || telephone.trim().equals(""))
            throw new BadRequestException("telephone required");
        if (email == null || email.trim().equals(""))
            throw new BadRequestException("email required");
        if (login == null || login.trim().equals(""))
            throw new BadRequestException("login required");
        if (nom == null || nom.trim().equals(""))
            throw new BadRequestException("nome required");
        if (prenom == null || prenom.trim().equals(""))
            throw new BadRequestException("telephone required");
    }

    @GetMapping("specialites/archive/{archive}")
    public ResponseEntity<?> getAllSpecialiteByArchive(@PathVariable Boolean archive) {
        if (archive == null) throw new BadRequestException("archive cannot be null");

        return ResponseEntity.ok(medecinPatientService.findAllSpecialite(archive));
    }

    @GetMapping("specialites/{id}")
    public ResponseEntity<?> getSpecialiteById(@PathVariable Long id) {
        if (id == null) throw new BadRequestException("id cannot be null");
        Specialite specialite = medecinPatientService.findSpecialiteById(id);
        if (specialite == null) throw new EntityNotFoundException("specialite not found");

        return ResponseEntity.ok(specialite);
    }

    @PostMapping("specialites")
    public ResponseEntity<?> addSpecialite(@RequestBody Specialite specialite) {
        if (specialite == null) throw new BadRequestException("specialite cannot be null");
        if (specialite.getLibelle() == null || specialite.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(specialite);
    }
}
