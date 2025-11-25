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

    @Autowired
    private NotificationService notificationService;

    public List<ForumPost> getAllPosts() {
        return forumPostRepository.findAllByOrderByCreatedAtAsc();
    }

    public Optional<ForumPost> getPostById(Long id) {
        return forumPostRepository.findById(id);
    }

    public List<ChatMessage> getMessagesByForumPostId(Long postId) {
        return chatMessageRepository.findByForumPostIdOrderByTimestampAsc(postId);
    }

    public ChatMessage addMessageToForumPost(Long postId, String content, String senderEmail) {
        ForumPost forumPost = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Forum post not found"));
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        ChatMessage message = new ChatMessage();
        message.setForumPost(forumPost);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        
        ChatMessage savedMessage = chatMessageRepository.save(message);

        // Уведомляем всех участников обсуждения
        List<User> participants = chatMessageRepository.findByForumPostId(postId)
                .stream()
                .map(ChatMessage::getSender)
                .distinct()
                .collect(java.util.stream.Collectors.toList());

        if (!participants.contains(forumPost.getAuthor())) {
            participants.add(forumPost.getAuthor());
        }

        for (User participant : participants) {
            if (!participant.getEmail().equals(senderEmail)) {
                String notificationMessage = sender.getName() + " прокомментировал пост: " + forumPost.getTitle();
                notificationService.createNotification(participant, notificationMessage, "FORUM");
            }
        }
        
        return savedMessage;
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

    public ForumPost likePost(Long postId, String userEmail) {
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (post.getUsersWhoLiked().contains(user)) {
            post.getUsersWhoLiked().remove(user);
            post.setLikes(post.getLikes() - 1);
        } else {
            post.getUsersWhoLiked().add(user);
            post.setLikes(post.getLikes() + 1);
        }
        return forumPostRepository.save(post);
    }

    public ForumPost dislikePost(Long postId, String userEmail) {
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // For simplicity, we'll just increment the dislike count.
        // A more complex implementation would track which users have disliked the post.
        post.setDislikes(post.getDislikes() + 1);
        return forumPostRepository.save(post);
    }

    public ChatMessage likeMessage(Long messageId, String userEmail) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (message.getUsersWhoLiked().contains(user)) {
            message.getUsersWhoLiked().remove(user);
        } else {
            message.getUsersWhoLiked().add(user);
            message.getUsersWhoDisliked().remove(user);
        }
        message.setLikes(message.getUsersWhoLiked().size());
        message.setDislikes(message.getUsersWhoDisliked().size());
        return chatMessageRepository.save(message);
    }

    public ChatMessage dislikeMessage(Long messageId, String userEmail) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (message.getUsersWhoDisliked().contains(user)) {
            message.getUsersWhoDisliked().remove(user);
        } else {
            message.getUsersWhoDisliked().add(user);
            message.getUsersWhoLiked().remove(user);
        }
        message.setLikes(message.getUsersWhoLiked().size());
        message.setDislikes(message.getUsersWhoDisliked().size());
        return chatMessageRepository.save(message);
    }
}
