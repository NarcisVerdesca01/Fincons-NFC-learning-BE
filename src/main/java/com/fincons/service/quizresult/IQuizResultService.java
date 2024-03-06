package com.fincons.service.quizresult;

import com.fincons.entity.QuizResults;
import java.util.List;
import java.util.Map;

public interface IQuizResultService {

    List<QuizResults> findAllResultsQuiz();

    QuizResults getQuizResultsById(long id);

    QuizResults calculateAndSave(long quizId, String userEmail, Map<Long,List<Long>> answersMap);

    //QuizResults calculateAndSave(long quizId, String userEmail, List<Integer> listAnswers);
}
