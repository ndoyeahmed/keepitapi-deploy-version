package com.keepit.keepitapi.rest.controllers.gestionpatients;

import com.keepit.keepitapi.entities.gestionpatients.Constante;
import com.keepit.keepitapi.entities.gestionpatients.HistoriqueConstante;
import com.keepit.keepitapi.entities.gestionpatients.Suivi;
import com.keepit.keepitapi.rest.exceptions.BadRequestException;
import com.keepit.keepitapi.rest.exceptions.EntityNotFoundException;
import com.keepit.keepitapi.rest.utils.Utilitaire;
import com.keepit.keepitapi.services.gestionpatients.GestionPatientService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

@RestController
@Log
@RequestMapping("/api/")
public class GestionPatientController {

    private GestionPatientService gestionPatientService;
    private Utilitaire utilitaire;

    @Autowired
    public GestionPatientController(GestionPatientService gestionPatientService,
                                    Utilitaire utilitaire) {
        this.gestionPatientService = gestionPatientService;
        this.utilitaire = utilitaire;
    }

    @GetMapping("constantes/archive/{archive}")
    public ResponseEntity<?> getAllConstantesByArchive(@PathVariable Boolean archive) {
        if (archive == null) throw new BadRequestException("archive cannot be null");

        return ResponseEntity.ok(gestionPatientService.findAllConstante(archive));
    }

    @GetMapping("constantes/{id}")
    public ResponseEntity<?> getConstanteById(@PathVariable Long id) {
        if (id == null) throw new BadRequestException("archive cannot be null");
        Constante constante = gestionPatientService.findConstanteById(id);
        if (constante == null) throw new EntityNotFoundException("constante not found");

        return ResponseEntity.ok(constante);
    }

    @PostMapping("constantes")
    public ResponseEntity<?> addConstante(@RequestBody Constante constante) {
        if (constante == null) throw new BadRequestException("constante cannot be null");
        if (constante.getLibelle() == null || constante.getLibelle().trim().equals(""))
            throw new BadRequestException("libelle required");
        if (constante.getSeuil() == null) throw new BadRequestException("seuil required");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gestionPatientService.addConstante(constante));
    }

    @GetMapping("suivi")
    public ResponseEntity<?> getAllSuivi() {
        return ResponseEntity.ok(gestionPatientService.findAllSuivi());
    }

    @GetMapping("suivi/{id}")
    public ResponseEntity<?> getSuiviById(@PathVariable Long id) {
        if (id == null) throw new BadRequestException("id cannot be null");

        Suivi suivi = gestionPatientService.findSuiviById(id);
        if (suivi == null) throw new EntityNotFoundException("suivi not found");

        return ResponseEntity.ok(suivi);
    }

    @GetMapping("suivi/medecin")
    public MappingJacksonValue findAllSuiviByMedecin() {
        return utilitaire.getFilter(gestionPatientService.findAllSuiviByMedecin(),
                "passwordFilter", "password");
    }

    @PostMapping("suivi")
    public ResponseEntity<?> addSuivi(@RequestBody Suivi suivi) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gestionPatientService.addSuivi(suivi));
    }

    @GetMapping("historique-constante")
    public ResponseEntity<?> getHistoriqueConstante() {
        return ResponseEntity.ok(gestionPatientService.findAllHistoriqueConstante());
    }

    @GetMapping("historique-constante/alerte")
    public MappingJacksonValue getHistoriqueConstanteByAlerteNotNull() {
        return utilitaire.getFilter(gestionPatientService.findAllHistoriqueConstanteByAlerteNotNull(),
                "passwordFilter", "password");
    }

    @GetMapping("historique-constante/alerte/medecin/{processed}")
    public MappingJacksonValue findAllByAlerteAndConnectedMedecin(@PathVariable Boolean processed) {
        if (processed == null) throw new BadRequestException("processed cannot be null");
        return utilitaire.getFilter(gestionPatientService.findAllHistoriqueByAlerteAndConnectedMedecin(processed),
                "passwordFilter", "password");
    }

    @GetMapping("historique-constante/{id}")
    public MappingJacksonValue findHistoriqueConstanteById(@PathVariable Long id) {
        if (id == null) throw new BadRequestException("id cannot be null");

        HistoriqueConstante historiqueConstante = gestionPatientService
                .findHistoriqueConstanteById(id);

        if (historiqueConstante == null)
            throw new EntityNotFoundException("historique-constante not found");

        return utilitaire.getFilter(historiqueConstante,
                "passwordFilter", "password");
    }

    @PostMapping("historique-constante")
    public ResponseEntity<?> addHistoriqueConstante(@RequestBody HistoriqueConstante historiqueConstante) {
        if (historiqueConstante == null) throw new BadRequestException("body cannot be null");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gestionPatientService.addHistoriqueConstante(historiqueConstante));
    }
}
