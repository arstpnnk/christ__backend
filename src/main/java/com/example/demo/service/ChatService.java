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

    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }

    public ChatMessage sendMessage(String content, String senderEmail) {
        Optional<User> sender = userRepository.findByEmail(senderEmail);
        if (sender.isEmpty()) {
            throw new RuntimeException("Sender not found");
        }
        ChatMessage message = new ChatMessage();
        message.setContent(content);
        message.setSender(sender.get());
        return chatMessageRepository.save(message);
    }
}
