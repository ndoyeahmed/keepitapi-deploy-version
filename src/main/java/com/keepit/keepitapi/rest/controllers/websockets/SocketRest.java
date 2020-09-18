package com.keepit.keepitapi.rest.controllers.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepit.keepitapi.entities.chats.Message;
import com.keepit.keepitapi.rest.exceptions.BadRequestException;
import com.keepit.keepitapi.services.chats.ChatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/socket")
@CrossOrigin("*")
public class SocketRest {

    private SimpMessagingTemplate simpMessagingTemplate;
    private ChatsService chatsService;

    @Autowired
    public SocketRest(SimpMessagingTemplate simpMessagingTemplate, ChatsService chatsService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatsService = chatsService;
    }

    @PostMapping
    public ResponseEntity<?> useSimpleRest(@RequestBody Map<String, String> message){
        if(message.containsKey("message")){
            //if the toId is present the message will be sent privately else broadcast it to all users
            sendMessage(message);
            return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @MessageMapping("/send/message")
    public Map<String, String> useSocketCommunication(String message){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> messageConverted = null;
        try {
            messageConverted = mapper.readValue(message, Map.class);
        } catch (IOException e) {
            messageConverted = null;
        }
        if(messageConverted!=null){
            sendMessage(messageConverted);
        }
        return messageConverted;
    }

    private void sendMessage(Map<String, String> message) {
        if(message.containsKey("toId") && message.get("toId")!=null && !message.get("toId").equals("")){
            this.simpMessagingTemplate.convertAndSend("/socket-publisher/"+message.get("toId"),message);
            this.simpMessagingTemplate.convertAndSend("/socket-publisher/"+message.get("fromId"),message);
        }else{
            this.simpMessagingTemplate.convertAndSend("/socket-publisher",message);
        }
    }

    @GetMapping("/list-discussion")
    public ResponseEntity<?> findAllDiscussionByEmail() {
        return ResponseEntity.ok(chatsService.findAllDiscussionByEmail());
    }

}
