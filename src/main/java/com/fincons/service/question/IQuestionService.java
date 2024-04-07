package com.fincons.service.question;

import com.fincons.dto.QuestionDto;
import com.fincons.entity.Question;
import java.util.List;

public interface IQuestionService {

    Question findById(long id);

    List<Question> findAllQuestion();

    List<Question> findAllQuestionWithoutQuiz();

    List<Question> findAllQuestionWithoutAnswers();

    Question createQuestion(QuestionDto questionDto);

    void deleteQuestion(Long id);

    Question updateQuestion(long id, QuestionDto questionDto);

}
