package com.fincons.service.question;

import com.fincons.dto.QuestionDto;
import com.fincons.entity.Content;
import com.fincons.entity.Question;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class QuestionService implements  IQuestionService{
    @Autowired
    private QuestionRepository questionRepository;
    @Override
    public Question findById(long id) {
        if (!questionRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The question does not exist!");
        }
        return questionRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<Question> findAllQuestion() {
        return questionRepository.findAllByDeletedFalse();
    }

    @Override
    public Question createQuestion(QuestionDto questionDto) {
           Question newQuestion= new Question();
           newQuestion.setTextQuestion(questionDto.getTextQuestion());
           newQuestion.setValueOfQuestion(questionDto.getValueOfQuestion());
           Question savedQuestion= questionRepository.save(newQuestion);
           return savedQuestion;
    }

    @Override
    public void deleteQuestion(long id) {
        if (!questionRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The question does not exist");
        }
        Question questionToDelete = questionRepository.findByIdAndDeletedFalse(id);
        questionToDelete.setDeleted(true);
        questionRepository.save(questionToDelete);
    }

    @Override
    public Question updateQuestion(long id, QuestionDto questionDto) {
        Question questionToModify = questionRepository.findByIdAndDeletedFalse(id);

        if(questionToModify == null ){
            throw new ResourceNotFoundException("Question does not exist");
        }

        if (questionDto.getTextQuestion() == null && questionDto.getValueOfQuestion()<=0) {
            throw new IllegalArgumentException("Text or score of question is null");
            //TODO-IMPLEMENTARE L'AGGIORNAMENTO DEL TYPE
        }
        return questionRepository.save(questionToModify);
    }
}
