package com.example.demo.service;

import com.example.demo.model.ChatMessage;
import com.example.demo.model.User;
import com.example.demo.repository.ChatMessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }

    public ChatMessage sendMessage(String content, String senderEmail) {
        Optional<User> senderOptional = userRepository.findByEmail(senderEmail);
        if (senderOptional.isEmpty()) {
            throw new RuntimeException("Sender not found");
        }
        User sender = senderOptional.get();
        ChatMessage message = new ChatMessage();
        message.setContent(content);
        message.setSender(sender);
        
        ChatMessage savedMessage = chatMessageRepository.save(message);

        // Notify all other users
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (!user.getEmail().equals(senderEmail)) {
                String notificationMessage = "New message in chat from " + sender.getName();
                notificationService.createNotification(user, notificationMessage, "CHAT");
            }
        }
        
        return savedMessage;
    }
}
