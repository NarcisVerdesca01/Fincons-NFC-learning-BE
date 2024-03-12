package com.fincons.service.answer;

import com.fincons.dto.AnswerDto;
import com.fincons.dto.QuestionDto;
import com.fincons.entity.Answer;
import com.fincons.entity.Question;
import com.fincons.entity.Quiz;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.QuestionMapper;
import com.fincons.repository.AnswerRepository;
import com.fincons.repository.QuestionRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AnswerService implements IAnswerService{

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public Answer findById(long id) {
        if (!answerRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The answer does not exist!");
        }
        return answerRepository.findByIdAndDeletedFalse(id);

    }

    @Override
    public List<Answer> findAllAnswer() {
        return answerRepository.findAllByDeletedFalse();
    }

    @Override
    public Answer createAnswer(AnswerDto answerDto) throws DuplicateException {

        if(StringUtils.isBlank(answerDto.getText())){
            throw new IllegalArgumentException("User must enter the text of answer!");
        }
        if (answerRepository.existsByTextAndDeletedFalse(answerDto.getText())) {
            throw new DuplicateException("The name of ability already exists");
        }
        Answer newAnswer= new Answer();
        newAnswer.setText(answerDto.getText());
        newAnswer.setCorrect(answerDto.isCorrect());
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
        Answer answerToModify = answerRepository.findByIdAndDeletedFalse(id);

        if(answerToModify == null){
            throw new ResourceNotFoundException("Answer does not exist");
        }

        if (answerDto.getText() == null || answerDto.getQuestion() == null) {
            throw new IllegalArgumentException("Text of answer is null");
            //TODO-IMPLEMENTARE L'AGGIORNAMENTO DEL TYPE
        }
        if(answerDto.getQuestion()!= null){
            QuestionDto question = answerDto.getQuestion();
            answerToModify.setQuestion(questionMapper.mapQuestionDtoToQuestionEntity(question));
        }
        return answerRepository.save(answerToModify);
    }

    @Override
    public Answer associateQuestionToAnswer(long idAnswer, long idQuestion) throws DuplicateException {
        Answer answerToAssociateQuestion = answerRepository.findByIdAndDeletedFalse(idAnswer);

        Question questionToAssociate = questionRepository.findByIdAndDeletedFalse(idAnswer);
        if(answerToAssociateQuestion == null){
            throw new ResourceNotFoundException("Answer does not exist");
        }
        if(questionToAssociate == null){
            throw new ResourceNotFoundException("Question does not exist");
        }

        /*
        if(answerToAssociateQuestion.getQuestion().getId()== idQuestion){
            throw new DuplicateException("The answer has already been associated with the question '"+ questionToAssociate.getTextQuestion()+ "'.");
        }*/

        answerToAssociateQuestion.setQuestion(questionToAssociate);

        return answerRepository.save(answerToAssociateQuestion);
    }
}
