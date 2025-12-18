package com.example.demo.repository;

import com.example.demo.model.Bookmark;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserAndBookIdOrderByCreatedAtDesc(User user, String bookId);
    Optional<Bookmark> findByUserAndExternalId(User user, String externalId);
    void deleteByUserAndExternalId(User user, String externalId);
}

