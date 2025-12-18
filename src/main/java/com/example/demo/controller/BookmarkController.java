package com.example.demo.controller;

import com.example.demo.model.Bookmark;
import com.example.demo.model.User;
import com.example.demo.service.BookmarkService;
import com.example.demo.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarks;

    public BookmarkController(BookmarkService bookmarks) {
        this.bookmarks = bookmarks;
    }

    @GetMapping
    public ResponseEntity<List<BookmarkDTO>> list(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String bookId
    ) {
        User user = userDetails.getUser();
        List<BookmarkDTO> out = bookmarks.list(user, bookId).stream()
                .map(BookmarkDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    @PostMapping
    public ResponseEntity<BookmarkDTO> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateBookmarkRequest body
    ) {
        User user = userDetails.getUser();
        Bookmark b = bookmarks.createOrGet(
                user,
                body.getExternalId(),
                body.getBookId(),
                body.getChapter(),
                body.getPageIndex(),
                body.getLineIndex(),
                body.getPreview()
        );
        return ResponseEntity.ok(BookmarkDTO.from(b));
    }

    @DeleteMapping("/external/{externalId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String externalId
    ) {
        bookmarks.delete(userDetails.getUser(), externalId);
        return ResponseEntity.ok().build();
    }
}

class CreateBookmarkRequest {
    private String externalId;
    private String bookId;
    private Integer chapter;
    private Integer pageIndex;
    private Integer lineIndex;
    private String preview;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Integer getChapter() {
        return chapter;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(Integer lineIndex) {
        this.lineIndex = lineIndex;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}

class BookmarkDTO {
    private String externalId;
    private String bookId;
    private Integer chapter;
    private Integer pageIndex;
    private Integer lineIndex;
    private String preview;
    private String createdAt;

    public static BookmarkDTO from(Bookmark b) {
        BookmarkDTO dto = new BookmarkDTO();
        dto.externalId = b.getExternalId();
        dto.bookId = b.getBookId();
        dto.chapter = b.getChapter();
        dto.pageIndex = b.getPageIndex();
        dto.lineIndex = b.getLineIndex();
        dto.preview = b.getPreview();
        dto.createdAt = b.getCreatedAt() != null ? b.getCreatedAt().toString() : null;
        return dto;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getBookId() {
        return bookId;
    }

    public Integer getChapter() {
        return chapter;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public Integer getLineIndex() {
        return lineIndex;
    }

    public String getPreview() {
        return preview;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

