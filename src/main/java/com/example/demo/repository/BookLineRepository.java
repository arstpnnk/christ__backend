package com.example.demo.repository;

import com.example.demo.model.BookLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookLineRepository extends JpaRepository<BookLine, Long> {
    List<BookLine> findByBookIdAndChapterOrderByLineIndex(String bookId, Integer chapter);
    boolean existsByBookIdAndChapter(String bookId, Integer chapter);
}

