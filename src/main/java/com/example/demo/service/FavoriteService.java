package com.example.demo.service;

import com.example.demo.model.Favorite;
import com.example.demo.model.User;
import com.example.demo.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository repo;

    public FavoriteService(FavoriteRepository repo) {
        this.repo = repo;
    }

    public List<Favorite> list(User user) {
        return repo.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public Favorite add(User user, String bookId) {
        return repo.findByUserAndBookId(user, bookId).orElseGet(() -> {
            Favorite f = new Favorite();
            f.setUser(user);
            f.setBookId(bookId);
            f.setCreatedAt(Instant.now());
            return repo.save(f);
        });
    }

    @Transactional
    public void remove(User user, String bookId) {
        repo.deleteByUserAndBookId(user, bookId);
    }
}

