package com.fincons.repository;

import com.fincons.entity.Question;
import com.fincons.entity.Quiz;
import com.fincons.entity.QuizResponse;
import com.fincons.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResponseRepository extends JpaRepository<QuizResponse,Long> {
    List<QuizResponse> findAllByDeletedFalse();

    QuizResponse findByUserAndQuizAndQuestionAndDeletedFalse(User user, Quiz quiz, Question question);
}
