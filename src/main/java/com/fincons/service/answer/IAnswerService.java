package com.fincons.service.answer;

import com.fincons.dto.AnswerDto;
import com.fincons.entity.Answer;
import com.fincons.exception.DuplicateException;
import java.util.List;

public interface IAnswerService {

    Answer findById(long id);

    List<Answer> findAllAnswer();

    List<Answer> findAllAnswerWithoutQuestion();

    Answer createAnswer(AnswerDto answerDto) throws DuplicateException;

    void deleteAnswer(long id);

    Answer updateAnswer(long id, AnswerDto answerDto);

    Answer associateQuestionToAnswer(long idAnswer, long idQuestion)  throws DuplicateException;


}
