package com.example.demo.service;

import com.example.demo.model.PriestQuestion;
import com.example.demo.model.PriestQuestionReply;
import com.example.demo.model.User;
import com.example.demo.repository.PriestQuestionRepository;
import com.example.demo.repository.PriestQuestionReplyRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PriestQuestionService {

    @Autowired
    private PriestQuestionRepository priestQuestionRepository;

    @Autowired
    private PriestQuestionReplyRepository priestQuestionReplyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public List<PriestQuestion> getAllQuestions() {
        return priestQuestionRepository.findAll();
    }

    public Optional<PriestQuestion> getQuestionById(Long id) {
        return priestQuestionRepository.findById(id);
    }

    public PriestQuestion createQuestion(String title, String questionContent, String authorEmail) {
        Optional<User> author = userRepository.findByEmail(authorEmail);
        if (author.isEmpty()) {
            throw new RuntimeException("Author not found");
        }
        PriestQuestion question = new PriestQuestion();
        question.setTitle(title);
        question.setQuestion(questionContent);
        question.setAuthor(author.get());
        question.setCreatedAt(LocalDateTime.now());
        return priestQuestionRepository.save(question);
    }

    public List<PriestQuestionReply> getRepliesByQuestionId(Long questionId) {
        return priestQuestionReplyRepository.findByQuestionIdOrderByCreatedAtAsc(questionId);
    }

    public PriestQuestionReply addReplyToQuestion(Long questionId, String content, String authorEmail) {
        PriestQuestion question = priestQuestionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Priest question not found"));
        User author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        PriestQuestionReply reply = new PriestQuestionReply();
        reply.setQuestion(question);
        reply.setAuthor(author);
        reply.setContent(content);
        reply.setCreatedAt(LocalDateTime.now());
        
        PriestQuestionReply savedReply = priestQuestionReplyRepository.save(reply);

        // Уведомляем автора вопроса
        if (!question.getAuthor().getEmail().equals(authorEmail)) {
            String notificationMessage = "Священник ответил на ваш вопрос: " + question.getTitle();
            notificationService.createNotification(question.getAuthor(), notificationMessage, "PRIEST_REPLY");
        }

        return savedReply;
    }
}
