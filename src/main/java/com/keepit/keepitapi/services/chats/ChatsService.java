package com.keepit.keepitapi.services.chats;

import com.keepit.keepitapi.entities.admin.Utilisateur;
import com.keepit.keepitapi.entities.chats.Alerte;
import com.keepit.keepitapi.entities.chats.Message;
import com.keepit.keepitapi.repositories.chats.AlerteRepository;
import com.keepit.keepitapi.repositories.chats.MessageRepository;
import com.keepit.keepitapi.services.admin.UtilisateurService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Log
@Transactional
public class ChatsService {

    private AlerteRepository alerteRepository;
    private MessageRepository messageRepository;
    private UtilisateurService utilisateurService;

    public ChatsService(AlerteRepository alerteRepository,
                                     MessageRepository messageRepository,
                        UtilisateurService utilisateurService) {
        this.alerteRepository = alerteRepository;
        this.messageRepository = messageRepository;
        this.utilisateurService = utilisateurService;
    }

    /**
     * Add new alerte
     * @param alerte to add
     * @return new item added or throw an exception
     */
    public Alerte addAlerte(Alerte alerte) {
        try {
            alerteRepository.save(alerte);
            return alerte;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * find alerte by id
     * @param id search reference
     * @return alerte if found or null if not
     */
    public Alerte findAlerteById(Long id) {
        return alerteRepository.findById(id)
                .orElse(null);
    }

    /**
     * find all alert
     * @return list alert
     */
    public List<Alerte> findAllAlerte() {
        return alerteRepository.findAll();
    }

    /**
     * add new message
     * @param message to add
     * @return added message or throw an exception
     */
    public Message addMessage(Message message) {
        try {
            messageRepository.save(message);
            return message;
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
            throw e;
        }
    }

    /**
     * find a message by id
     * @param id search reference
     * @return message if found or null if not
     */
    public Message findMessageById(Long id) {
        return messageRepository.findById(id)
                .orElse(null);
    }

    /**
     * find all message
     * @return list of message
     */
    public List<Message> findAllMessage() {
        return messageRepository.findAll();
    }

    /**
     * find all message by a user email
     * @return list of message
     */
    public List<Message> findAllDiscussionByEmail() {
        Utilisateur utilisateur = utilisateurService.connectedUser();
        return messageRepository.findAllByUtilisateurAndDestinataire(utilisateur.getEmail())
                .orElse(new ArrayList<>());
    }
}
