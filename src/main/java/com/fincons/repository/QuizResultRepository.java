package com.fincons.repository;

import com.fincons.entity.Quiz;
import com.fincons.entity.QuizResults;
import com.fincons.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizResultRepository extends JpaRepository<QuizResults, Long> {

    boolean existsByUserAndQuiz(User user, Quiz quiz);
}
