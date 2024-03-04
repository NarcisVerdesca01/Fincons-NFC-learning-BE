package com.fincons.service.quizresult;

import com.fincons.dto.QuizResultsDto;
import com.fincons.entity.QuizResults;

import java.util.Arrays;
import java.util.List;

public interface IQuizResultService {

    List<QuizResults> findAllResultsQuiz();

    QuizResults getQuizResultsById(long id);

    QuizResults calculateAndSave(long quizId, String userEmail, List<Integer> listAnswers);
}
