package com.fincons.repository;

import com.fincons.entity.Quiz;
import com.fincons.entity.QuizResults;
import com.fincons.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResults, Long> {

    boolean existsByUserAndQuiz(User user, Quiz quiz);

    boolean existsByIdAndDeletedFalse(long id);

    QuizResults findByIdAndDeletedFalse(long id);

    boolean existsByUserAndQuizAndDeletedFalse(User user, Quiz quiz);

    List<QuizResults> findAllByDeletedFalse();

    QuizResults findByUserAndQuizAndDeletedFalse(User user, Quiz quiz); //appena aggiunto
}
