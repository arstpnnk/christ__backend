package com.example.demo.controller;

import com.example.demo.model.Note;
import com.example.demo.model.User;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteRepository notes;
    private final UserRepository users;

    public NoteController(NoteRepository notes, UserRepository users) {
        this.notes = notes;
        this.users = users;
    }

    @PostMapping
    public Note create(@RequestBody Note note, @AuthenticationPrincipal UserDetails principal) {
        User u = users.findByEmail(principal.getUsername()).orElseThrow();
        note.setUser(u);
        return notes.save(note);
    }

    @GetMapping
    public List<Note> list(@AuthenticationPrincipal UserDetails principal) {
        User u = users.findByEmail(principal.getUsername()).orElseThrow();
        return notes.findByUser(u);
    }

    

        @DeleteMapping("/{id}")

        public void delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {

            Note n = notes.findById(id).orElseThrow();

            if (!n.getUser().getEmail().equals(principal.getUsername())) throw new RuntimeException("forbidden");

            notes.deleteById(id);

        }

    

        @PutMapping("/{id}")

        public Note update(@PathVariable Long id, @RequestBody Note note, @AuthenticationPrincipal UserDetails principal) {

            Note n = notes.findById(id).orElseThrow();

            if (!n.getUser().getEmail().equals(principal.getUsername())) throw new RuntimeException("forbidden");

            n.setContent(note.getContent());

            return notes.save(n);

        }

    }

    