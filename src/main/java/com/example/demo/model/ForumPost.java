package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.example.demo.model.ChatMessage;
import com.example.demo.model.User;

@Entity
@Table(name = "forum_posts")
public class ForumPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "forumPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ChatMessage> messages;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int likes = 0;
    @Column(nullable = false, columnDefinition = "int default 0")
    private int dislikes = 0;

    @ManyToMany(mappedBy = "likedPosts")
    private Set<User> usersWhoLiked = new HashSet<>();

    public ForumPost() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public Set<User> getUsersWhoLiked() {
        return usersWhoLiked;
    }

    public void setUsersWhoLiked(Set<User> usersWhoLiked) {
        this.usersWhoLiked = usersWhoLiked;
    }
}
