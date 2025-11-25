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

    @PostMapping("/{postId}/like")
    public ResponseEntity<ForumPost> likePost(@PathVariable Long postId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(null);
        }
        try {
            ForumPost updatedPost = forumService.likePost(postId, principal.getName());
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{postId}/dislike")
    public ResponseEntity<ForumPost> dislikePost(@PathVariable Long postId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(null);
        }
        try {
            ForumPost updatedPost = forumService.dislikePost(postId, principal.getName());
            return ResponseEntity.ok(updatedPost);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{postId}/messages/{messageId}/like")
    public ResponseEntity<ChatMessage> likeMessage(@PathVariable Long postId, @PathVariable Long messageId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(null);
        }
        try {
            ChatMessage updatedMessage = forumService.likeMessage(messageId, principal.getName());
            return ResponseEntity.ok(updatedMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{postId}/messages/{messageId}/dislike")
    public ResponseEntity<ChatMessage> dislikeMessage(@PathVariable Long postId, @PathVariable Long messageId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(null);
        }
        try {
            ChatMessage updatedMessage = forumService.dislikeMessage(messageId, principal.getName());
            return ResponseEntity.ok(updatedMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

