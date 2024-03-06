package com.fincons.repository;

import com.fincons.entity.QuizResults;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizResultRepository extends JpaRepository<QuizResults, Long> {
}
