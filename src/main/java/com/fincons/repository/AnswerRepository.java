package com.fincons.repository;

import com.fincons.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
    boolean existsByIdAndDeletedFalse(long id);

    Answer findByIdAndDeletedFalse(long id);

    List<Answer> findAllByDeletedFalse();

    boolean existsByTextAndDeletedFalse(String text);
}
