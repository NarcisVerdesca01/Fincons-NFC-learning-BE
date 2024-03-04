package com.fincons.service.answer;

import com.fincons.dto.AnswerDto;
import com.fincons.entity.Answer;
import com.fincons.entity.Question;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnswerService implements IAnswerService{

    @Autowired
    private AnswerRepository answerRepository;


    @Override
    public Answer findById(long id) {
        if (!answerRepository.existsById(id)) {
            throw new ResourceNotFoundException("The answer does not exist!");
        }
        return answerRepository.findById(id).orElse(null);

    }

    @Override
    public List<Answer> findAllAnswer() {
        return answerRepository.findAll();
    }

    @Override
    public Answer createAnswer(AnswerDto answerDto) {
        Answer newAnswer= new Answer();
        newAnswer.setText(answerDto.getText());
        newAnswer.setQuestion(answerDto.getQuestion());
        Answer savedAnswer= answerRepository.save(newAnswer);
        return savedAnswer;
    }

    @Override
    public void deleteAnswer(long id) {
        if (!answerRepository.existsById(id)) {
            throw new ResourceNotFoundException("The answer does not exist");
        }

        // Elimina la risposta
        answerRepository.deleteById(id);
    }

    @Override
    public Answer updateAnswer(long id, AnswerDto answerDto) {
        Answer answerToModify = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer does not exist. "));

        if (answerDto.getText() == null || answerDto.getQuestion()== null) {
            throw new IllegalArgumentException("Text of answer is null");
            //TODO-IMPLEMENTARE L'AGGIORNAMENTO DEL TYPE
        }
        if(answerDto.getQuestion()!= null){
            Question question = answerDto.getQuestion();
            answerToModify.setQuestion(question);
        }
        return answerRepository.save(answerToModify);
    }
}
