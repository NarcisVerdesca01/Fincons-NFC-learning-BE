package com.fincons.service.quizresponse;

import com.fincons.entity.QuizResponse;
import com.fincons.repository.QuizResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuizResponseService implements IQuizResponseService {

    @Autowired
    private QuizResponseRepository quizResponseRepository;

    @Override
    public List<QuizResponse> findAllQuizResponse() {
        return quizResponseRepository.findAllByDeletedFalse();
    }
}
