package com.example.demo.repository;

import com.example.demo.model.PriestQuestionReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriestQuestionReplyRepository extends JpaRepository<PriestQuestionReply, Long> {
    List<PriestQuestionReply> findByQuestionIdOrderByCreatedAtAsc(Long questionId);
}
