package com.fincons.service.answer;

import com.fincons.dto.AnswerDto;
import com.fincons.dto.ContentDto;
import com.fincons.entity.Answer;
import com.fincons.entity.Content;

import java.util.List;

public interface IAnswerService {
    Answer findById(long id);
    List<Answer> findAllAnswer();
    Answer createAnswer(AnswerDto answerDto);
    void deleteAnswer(long id);
    Answer updateAnswer(long id, AnswerDto answerDto);

}
