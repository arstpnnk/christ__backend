package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(
        name = "book_lines",
        uniqueConstraints = @UniqueConstraint(columnNames = {"book_id", "chapter", "line_index"})
)
public class BookLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id", nullable = false)
    private String bookId;

    @Column(nullable = false)
    private Integer chapter;

    @Column(name = "line_index", nullable = false)
    private Integer lineIndex;

    @Column(nullable = false, length = 2000)
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(Integer lineIndex) {
        this.lineIndex = lineIndex;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
