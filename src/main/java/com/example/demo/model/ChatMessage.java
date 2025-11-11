package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "forum_post_id")
    @JsonBackReference
    private ForumPost forumPost;

    @Column(nullable = false)
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int likes = 0;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int dislikes = 0;

    @ManyToMany
    @JoinTable(
            name = "chat_message_likes",
            joinColumns = @JoinColumn(name = "chat_message_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> usersWhoLiked = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "chat_message_dislikes",
            joinColumns = @JoinColumn(name = "chat_message_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> usersWhoDisliked = new HashSet<>();

    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public ForumPost getForumPost() {
        return forumPost;
    }

    public void setForumPost(ForumPost forumPost) {
        this.forumPost = forumPost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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

    public Set<User> getUsersWhoDisliked() {
        return usersWhoDisliked;
    }

    public void setUsersWhoDisliked(Set<User> usersWhoDisliked) {
        this.usersWhoDisliked = usersWhoDisliked;
    }
}
