package com.example.demo.repository;

import com.example.demo.model.PriestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriestQuestionRepository extends JpaRepository<PriestQuestion, Long> {
    List<PriestQuestion> findByAuthorId(Long authorId);
    List<PriestQuestion> findAllByOrderByCreatedAtAsc();
}
