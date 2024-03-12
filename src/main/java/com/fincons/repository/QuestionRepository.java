package com.fincons.repository;

import com.fincons.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findByIdAndDeletedFalse(long idAnswer);

    boolean existsByIdAndDeletedFalse(long id);

    List<Question> findAllByDeletedFalse();
}
