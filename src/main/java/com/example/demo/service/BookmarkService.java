package com.example.demo.service;

import com.example.demo.model.Bookmark;
import com.example.demo.model.User;
import com.example.demo.repository.BookmarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class BookmarkService {
    private final BookmarkRepository repo;

    public BookmarkService(BookmarkRepository repo) {
        this.repo = repo;
    }

    public List<Bookmark> list(User user, String bookId) {
        return repo.findByUserAndBookIdOrderByCreatedAtDesc(user, bookId);
    }

    @Transactional
    public Bookmark createOrGet(User user, String externalId, String bookId, int chapter, int pageIndex, int lineIndex, String preview) {
        return repo.findByUserAndExternalId(user, externalId).orElseGet(() -> {
            Bookmark b = new Bookmark();
            b.setUser(user);
            b.setExternalId(externalId);
            b.setBookId(bookId);
            b.setChapter(chapter);
            b.setPageIndex(pageIndex);
            b.setLineIndex(lineIndex);
            b.setPreview(preview);
            b.setCreatedAt(Instant.now());
            return repo.save(b);
        });
    }

    @Transactional
    public void delete(User user, String externalId) {
        repo.deleteByUserAndExternalId(user, externalId);
    }
}

