package com.fincons.service.quiz;

import com.fincons.dto.QuestionDto;
import com.fincons.dto.QuizDto;
import com.fincons.entity.Question;
import com.fincons.entity.Quiz;

import java.util.List;

public interface IQuizService {
    Quiz findById(long id);
    List<Quiz> findAllQuiz();
    Quiz createQuiz(QuizDto quizDto);
    void deleteQuiz(long id);
    Quiz updateQuiz(long id, QuizDto quizDto);
}
