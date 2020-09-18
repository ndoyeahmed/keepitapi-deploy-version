package com.keepit.keepitapi.rest.controllers.admin;


import com.keepit.keepitapi.entities.admin.Medecin;
import com.keepit.keepitapi.entities.admin.Patient;
import com.keepit.keepitapi.entities.admin.Profil;
import com.keepit.keepitapi.entities.admin.Utilisateur;
import com.keepit.keepitapi.rest.exceptions.BadRequestException;
import com.keepit.keepitapi.rest.exceptions.EntityNotFoundException;
import com.keepit.keepitapi.rest.pojo.JWTToken;
import com.keepit.keepitapi.rest.pojo.LoginModel;
import com.keepit.keepitapi.rest.utils.Utilitaire;
import com.keepit.keepitapi.security.TokenProvider;
import com.keepit.keepitapi.services.admin.MedecinPatientService;
import com.keepit.keepitapi.services.admin.UtilisateurService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@Log
@RequestMapping("/api")
public class UtilisateurController {

    private AuthenticationManager authenticationManager;
    private TokenProvider tokenProvider;
    private UtilisateurService utilisateurService;
    private Utilitaire utilitaire;
    private MedecinPatientService medecinPatientService;

    @Autowired
    public UtilisateurController(AuthenticationManager authenticationManager, TokenProvider tokenProvider,
                                 UtilisateurService utilisateurService, Utilitaire utilitaire,
                                 MedecinPatientService medecinPatientService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.utilisateurService = utilisateurService;
        this.utilitaire = utilitaire;
        this.medecinPatientService = medecinPatientService;
    }

    @RequestMapping(value = "/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginModel loginModel, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginModel.getLogin(), loginModel.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Map<String, String> jwt = tokenProvider.createToken(authentication);
            response.addHeader(TokenProvider.HEADER, "Bearer " + jwt);
            return ResponseEntity.ok(new JWTToken(jwt.get("id_token"), Long.valueOf(jwt.get("expires_at"))));
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("AuthenticationException", e.getLocalizedMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body, HttpServletResponse response) {
        if (body == null) throw new BadRequestException("body cannot be null");
        if (body.get("username") == null || body.get("username").trim().equals(""))
            throw new BadRequestException("username required");
        if (body.get("email") == null || body.get("email").trim().equals(""))
            throw new BadRequestException("email required");
        if (body.get("telephone") == null || body.get("telephone").trim().equals(""))
            throw new BadRequestException("telephone required");
        if (body.get("password") == null || body.get("password").trim().equals(""))
            throw new BadRequestException("password required");

        int typeUser = Integer.parseInt(body.get("typeUser"));
        if (typeUser == 0) {
            Utilisateur utilisateur = new Utilisateur();
            return ResponseEntity.status(HttpStatus.CREATED).body(utilisateur);
        } else if (typeUser == 1) {
            Medecin medecin = new Medecin();
            medecin.setLogin(body.get("username"));
            medecin.setEmail(body.get("email"));
            medecin.setTelephone(body.get("telephone"));
            medecin.setPassword(utilisateurService.encodePassword(body.get("password")));
            medecin.setPasswordChange(true);
            medecin.setArchive(false);
            medecin.setStatus(true);
            Profil profil = utilisateurService.findProfilByLibelle("MEDECIN");
            medecin.setProfil(profil);

            medecinPatientService.addMedecin(medecin);
            LoginModel loginModel = new LoginModel();
            loginModel.setLogin(medecin.getLogin());
            loginModel.setPassword(body.get("password"));
            return login(loginModel, response);
        } else {
            Patient patient = new Patient();
            return ResponseEntity.status(HttpStatus.CREATED).body(patient);
        }
    }

    @GetMapping("/connected-user")
    public MappingJacksonValue connectedUser() {
        try {
            Utilisateur utilisateur = utilisateurService.connectedUser();
            return utilitaire.getFilter(utilisateur, "passwordFilter", "password");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



    @PostMapping("/reset-password")
    public MappingJacksonValue resetUserPassword(@RequestBody Map<String, String> body) {
        try {
            Utilisateur utilisateurs = utilisateurService.resetUserPassword(body);

            return utilitaire.getFilter(utilisateurs, "passwordFilter", "password");

        } catch (BadRequestException e) {
            log.severe(e.getMessage());
            throw e;
        }
    }

    @GetMapping("/utilisateurs/{archive}")
    public MappingJacksonValue allUsers(@PathVariable boolean archive) {
        List<Utilisateur> utilisateurs = utilisateurService.findAllByArchiveFalseAndStatutTrue(archive);

        return utilitaire.getFilter(utilisateurs, "passwordFilter", "password");
    }

    @GetMapping("/utilisateurs/archive-statut/{archive}/{statut}")
    public MappingJacksonValue allUsersByArchiveAndStatut(@PathVariable boolean archive, @PathVariable boolean statut) {
        List<Utilisateur> utilisateurs = utilisateurService.findAllUserByArchiveAndStatut(archive, statut);

        return utilitaire.getFilter(utilisateurs, "passwordFilter", "password");
    }

    @GetMapping("/profils")
    public ResponseEntity<?> allProfil() {
        return ResponseEntity.ok(utilisateurService.findAllProfil());
    }

    @GetMapping("/utilisateurs-by-id/{id}")
    public MappingJacksonValue findByIdAndArchiveFalseAndStatutTrue(@PathVariable Long id) {
        try {
            return utilitaire.getFilter(utilisateurService.findUserByIdAndArchiveFalseAndStatutTrue(id), "passwordFilter", "password");
        } catch (EntityNotFoundException e) {
            log.severe(e.getMessage());
            throw e;
        }
    }


    @PutMapping("/utilisateurs-status/{id}")
    public MappingJacksonValue updateUserStatus(@RequestBody Map<String, String> body, @PathVariable Long id) {
        try {
            return utilitaire.getFilter(utilisateurService.updateStatutUser(body, id), "passwordFilter", "password");
        } catch (EntityNotFoundException e) {
            log.severe(e.getMessage());
            throw e;
        }
    }
}
