package com.example.demo.controller;

import com.example.demo.model.PriestQuestion;
import com.example.demo.model.PriestQuestionReply;
import com.example.demo.service.PriestQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/priest-questions")
public class PriestQuestionController {

    @Autowired
    private PriestQuestionService priestQuestionService;

    @GetMapping
    public List<PriestQuestion> getAllQuestions() {
        return priestQuestionService.getAllQuestions();
    }

    @PostMapping
    public ResponseEntity<?> createQuestion(@RequestBody PriestQuestionRequest questionRequest, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            PriestQuestion newQuestion = priestQuestionService.createQuestion(questionRequest.getTitle(), questionRequest.getQuestion(), principal.getName());
            return ResponseEntity.ok(newQuestion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{questionId}/replies")
    public ResponseEntity<List<PriestQuestionReply>> getQuestionReplies(@PathVariable Long questionId) {
        List<PriestQuestionReply> replies = priestQuestionService.getRepliesByQuestionId(questionId);
        return ResponseEntity.ok(replies);
    }

    @PostMapping("/{questionId}/replies")
    public ResponseEntity<?> addReplyToQuestion(
            @PathVariable Long questionId,
            @RequestBody PriestQuestionReplyRequest replyRequest,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            PriestQuestionReply newReply = priestQuestionService.addReplyToQuestion(questionId, replyRequest.getContent(), principal.getName());
            return ResponseEntity.ok(newReply);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

class PriestQuestionRequest {
    private String title;
    private String question;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

class PriestQuestionReplyRequest {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
