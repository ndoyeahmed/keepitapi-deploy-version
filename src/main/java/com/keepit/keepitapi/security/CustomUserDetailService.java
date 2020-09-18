package com.keepit.keepitapi.security;

import com.keepit.keepitapi.entities.admin.Utilisateur;
import com.keepit.keepitapi.repositories.admin.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomUserDetailService implements UserDetailsService {

    private UtilisateurRepository repository;

    @Autowired
    public void setRepository(UtilisateurRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Utilisateur> userSearch = repository.connexion(login, true);
        return userSearch.map(utilisateur -> {
            List<GrantedAuthority> profils = new ArrayList<>();
            profils.add(new SimpleGrantedAuthority(utilisateur.getProfil().getLibelle()));
            return new User(login, utilisateur.getPassword(), profils);
        }).orElseThrow(() -> new UsernameNotFoundException("L'utilisateur " + login + " n'existe pas !"));
    }
}
