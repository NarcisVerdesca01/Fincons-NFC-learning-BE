package com.fincons.service.quiz;

import com.fincons.dto.QuestionDto;
import com.fincons.dto.QuizDto;
import com.fincons.entity.Question;
import com.fincons.entity.Quiz;
import com.fincons.exception.DuplicateException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface IQuizService {
    Quiz findById(long id);
    List<Quiz> findAllQuiz();
    List<Quiz> findAllQuizWithoutLesson();
    Quiz createQuiz(QuizDto quizDto) throws DuplicateException;
    void deleteQuiz(long id);
    Quiz updateQuiz(long id, QuizDto quizDto);

    Quiz associateLesson(long idQuiz, long idLesson)  throws DuplicateException;

    Quiz associateQuestion(long idQuiz, long idLesson) throws  DuplicateException;
}
