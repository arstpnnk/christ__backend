package com.example.demo.controller;

import com.example.demo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService books;

    public BookController(BookService books) {
        this.books = books;
    }

    @GetMapping("/{bookId}/chapters/{chapter}")
    public ResponseEntity<BookChapterResponse> getChapter(
            @PathVariable String bookId,
            @PathVariable Integer chapter
    ) {
        String title = books.getChapterTitle(bookId, chapter);
        List<String> lines = books.getChapterLines(bookId, chapter);
        return ResponseEntity.ok(new BookChapterResponse(bookId, chapter, title, lines));
    }
}

class BookChapterResponse {
    private final String bookId;
    private final Integer chapter;
    private final String title;
    private final List<String> lines;

    public BookChapterResponse(String bookId, Integer chapter, String title, List<String> lines) {
        this.bookId = bookId;
        this.chapter = chapter;
        this.title = title;
        this.lines = lines;
    }

    public String getBookId() {
        return bookId;
    }

    public Integer getChapter() {
        return chapter;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLines() {
        return lines;
    }
}

