package com.example.demo.repository;

import com.example.demo.model.Favorite;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserOrderByCreatedAtDesc(User user);
    Optional<Favorite> findByUserAndBookId(User user, String bookId);
    void deleteByUserAndBookId(User user, String bookId);
}

