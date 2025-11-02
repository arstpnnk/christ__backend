package com.example.demo.controller;

import com.example.demo.model.ChatMessage;
import com.example.demo.model.ForumPost;
import com.example.demo.controller.ChatMessageRequest;
import com.example.demo.controller.ForumPostRequest;
import com.example.demo.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @GetMapping
    public List<ForumPost> getAllPosts() {
        return forumService.getAllPosts();
    }

    @GetMapping("/{postId}/messages")
    public ResponseEntity<List<ChatMessage>> getForumPostMessages(@PathVariable Long postId) {
        List<ChatMessage> messages = forumService.getMessagesByForumPostId(postId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{postId}/messages")
    public ResponseEntity<?> addMessageToForumPost(
            @PathVariable Long postId,
            @RequestBody ChatMessageRequest messageRequest,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            ChatMessage newMessage = forumService.addMessageToForumPost(postId, messageRequest.getContent(), principal.getName());
            return ResponseEntity.ok(newMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody ForumPostRequest postRequest, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            ForumPost newPost = forumService.createPost(postRequest.getTitle(), postRequest.getContent(), principal.getName());
            return ResponseEntity.ok(newPost);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        forumService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}

