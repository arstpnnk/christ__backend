package com.example.demo.service;

import com.example.demo.model.ForumPost;
import com.example.demo.model.User;
import com.example.demo.repository.ForumPostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.ChatMessage;
import com.example.demo.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ForumService {

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public List<ForumPost> getAllPosts() {
        return forumPostRepository.findAll();
    }

    public Optional<ForumPost> getPostById(Long id) {
        return forumPostRepository.findById(id);
    }

    public List<ChatMessage> getMessagesByForumPostId(Long postId) {
        return chatMessageRepository.findByForumPostIdOrderByTimestampAsc(postId);
    }

    public ChatMessage addMessageToForumPost(Long postId, String content, String senderEmail) {
        Optional<ForumPost> forumPost = forumPostRepository.findById(postId);
        if (forumPost.isEmpty()) {
            throw new RuntimeException("Forum post not found");
        }
        Optional<User> sender = userRepository.findByEmail(senderEmail);
        if (sender.isEmpty()) {
            throw new RuntimeException("Sender not found");
        }

        ChatMessage message = new ChatMessage();
        message.setForumPost(forumPost.get());
        message.setSender(sender.get());
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return chatMessageRepository.save(message);
    }

    public ForumPost createPost(String title, String content, String authorEmail) {
        Optional<User> author = userRepository.findByEmail(authorEmail);
        if (author.isEmpty()) {
            throw new RuntimeException("Author not found");
        }
        ForumPost post = new ForumPost();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author.get());
        return forumPostRepository.save(post);
    }

    public void deletePost(Long id) {
        forumPostRepository.deleteById(id);
    }
}
