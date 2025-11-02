package com.example.demo.controller;

import com.example.demo.model.ChatMessage;
import com.example.demo.controller.ChatMessageRequest;
import com.example.demo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public List<ChatMessage> getAllMessages() {
        return chatService.getAllMessages();
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageRequest messageRequest, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            ChatMessage newMessage = chatService.sendMessage(messageRequest.getContent(), principal.getName());
            return ResponseEntity.ok(newMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
