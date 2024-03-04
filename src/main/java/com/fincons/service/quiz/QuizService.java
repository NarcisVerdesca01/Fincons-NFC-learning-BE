package com.fincons.service.quiz;

import com.fincons.dto.QuizDto;
import com.fincons.entity.Question;
import com.fincons.entity.Quiz;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService implements IQuizService{

      @Autowired
      private QuizRepository quizRepository;
    @Override
    public Quiz findById(long id) {
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("The quiz does not exist!");
        }
        return quizRepository.findById(id).orElse(null);
    }

    @Override
    public List<Quiz> findAllQuiz() {
        return quizRepository.findAll();
    }

    @Override
    public Quiz createQuiz(QuizDto quizDto) {
        Quiz newQuiz= new Quiz();
        newQuiz.setTitle(quizDto.getTitle());


        Quiz savedQuiz= quizRepository.save(newQuiz);
        return savedQuiz;
    }

    @Override
    public void deleteQuiz(long id) {
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("The quiz does not exist");
        }

        // Elimina il quiz
        quizRepository.deleteById(id);
    }

    @Override
    public Quiz updateQuiz(long id, QuizDto quizDto) {
        Quiz quizToModify = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz does not exist. "));

        if (quizDto.getTitle() == null) {
            throw new IllegalArgumentException("Title of quiz is null");
            //TODO-IMPLEMENTARE L'AGGIORNAMENTO DEL TYPE
        }
        return quizRepository.save(quizToModify);
    }
}
