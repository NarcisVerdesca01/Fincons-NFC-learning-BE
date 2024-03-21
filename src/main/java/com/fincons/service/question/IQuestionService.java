package com.fincons.service.question;

import com.fincons.dto.ContentDto;
import com.fincons.dto.QuestionDto;
import com.fincons.entity.Content;
import com.fincons.entity.Question;

import java.util.List;

public interface IQuestionService {
    Question findById(long id);
    List<Question> findAllQuestion();

    List<Question> findAllQuestionWithoutQuiz();
    List<Question> findAllQuestionWithoutAnswers();
    Question createQuestion(QuestionDto questionDto);
    void deleteQuestion(long id);
    Question updateQuestion(long id, QuestionDto questionDto);
}
