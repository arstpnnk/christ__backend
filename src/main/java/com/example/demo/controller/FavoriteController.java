package com.example.demo.controller;

import com.example.demo.model.Favorite;
import com.example.demo.model.User;
import com.example.demo.service.CustomUserDetails;
import com.example.demo.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favorites;

    public FavoriteController(FavoriteService favorites) {
        this.favorites = favorites;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteDTO>> list(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        List<FavoriteDTO> out = favorites.list(user).stream().map(FavoriteDTO::from).collect(Collectors.toList());
        return ResponseEntity.ok(out);
    }

    @PostMapping
    public ResponseEntity<FavoriteDTO> add(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody FavoriteRequest body
    ) {
        Favorite f = favorites.add(userDetails.getUser(), body.getBookId());
        return ResponseEntity.ok(FavoriteDTO.from(f));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> remove(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String bookId
    ) {
        favorites.remove(userDetails.getUser(), bookId);
        return ResponseEntity.ok().build();
    }
}

class FavoriteRequest {
    private String bookId;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}

class FavoriteDTO {
    private String bookId;
    private String createdAt;

    public static FavoriteDTO from(Favorite f) {
        FavoriteDTO dto = new FavoriteDTO();
        dto.bookId = f.getBookId();
        dto.createdAt = f.getCreatedAt() != null ? f.getCreatedAt().toString() : null;
        return dto;
    }

    public String getBookId() {
        return bookId;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

