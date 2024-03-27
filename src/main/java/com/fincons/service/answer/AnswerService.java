package com.fincons.service.answer;

import com.fincons.dto.AnswerDto;
import com.fincons.dto.QuestionDto;
import com.fincons.entity.AbilityUser;
import com.fincons.entity.Answer;
import com.fincons.entity.Question;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.QuestionMapper;
import com.fincons.repository.AnswerRepository;
import com.fincons.repository.QuestionRepository;
import com.fincons.utility.TitleOrDescriptionValidator;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<Answer> findAllAnswerWithoutQuestion() {
        return answerRepository.findAllByDeletedFalseAndQuestionIsNull();
    }

    @Override
    public Answer createAnswer(AnswerDto answerDto) throws DuplicateException {

        checkBlank(answerDto);
        checkNameExistence(answerDto);
        checkTitleValidity(answerDto);

        Answer newAnswer= new Answer();
        newAnswer.setText(answerDto.getText());
        newAnswer.setCorrect(answerDto.isCorrect());
        Answer savedAnswer= answerRepository.save(newAnswer);
        return savedAnswer;
    }

    private static void checkTitleValidity(AnswerDto answerDto) {
        if (!TitleOrDescriptionValidator.isValidTitle(answerDto.getText())) {
            throw new IllegalArgumentException("The name of answer doesn't respect rules");
        }
    }

    private void checkNameExistence(AnswerDto answerDto) throws DuplicateException {
        if (answerRepository.existsByTextAndDeletedFalse(answerDto.getText())) {
            throw new DuplicateException("The answer already exists");
        }
    }

    private static void checkBlank(AnswerDto answerDto) {
        if(StringUtils.isBlank(answerDto.getText())){
            throw new IllegalArgumentException("User must enter the text of answer!");
        }
    }


    @Override
    public void deleteAnswer(long id) {
        if (!answerRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The answer does not exist");
        }
        Answer answerToDelete = answerRepository.findByIdAndDeletedFalse(id);
        answerToDelete.setDeleted(true);
        answerRepository.save(answerToDelete);

//        if (!questionRepository.existsByIdAndDeletedFalse(id)) {
//            throw new ResourceNotFoundException("The question does not exist");
//        }
//        Question questionToDelete = questionRepository.findByIdAndDeletedFalse(id);
//        questionToDelete.setDeleted(true);
//        questionRepository.save(questionToDelete);
//
//        List<Answer> answersToSetQuestionNull = answerRepository.findAllByDeletedFalse()
//                .stream()
//                .filter(answer-> id.equals(Optional.of(answer).map(Answer::getQuestion).map(Question::getId).orElse(null)))
//                .toList();
//
//        answersToSetQuestionNull
//                .forEach(answer->answer.setQuestion(null));
//
//        answersToSetQuestionNull
//                .forEach(answer -> answerRepository.save(answer));
//


    }

    @Override
    public Answer updateAnswer(long id, AnswerDto answerDto) {
        Answer answerToModify = answerRepository.findByIdAndDeletedFalse(id);

        if(answerToModify == null){
            throw new ResourceNotFoundException("Answer does not exist");
        }

        if (answerDto.getText() == null) {
            throw new IllegalArgumentException("Text of answer is null");
            //TODO-IMPLEMENTARE L'AGGIORNAMENTO DEL TYPE
        }
        if(answerDto.getQuestion()!= null){
            QuestionDto question = answerDto.getQuestion();
            answerToModify.setQuestion(questionMapper.mapQuestionDtoToQuestionEntity(question));
        }
        answerToModify.setText(answerDto.getText());

        Boolean isCorrect= answerDto.isCorrect();

        if(isCorrect!=null){
        answerToModify.setCorrect(answerDto.isCorrect());
        }
        return answerRepository.save(answerToModify);
    }

    @Override
    public Answer associateQuestionToAnswer(long idAnswer, long idQuestion) throws DuplicateException {
        Answer answerToAssociateQuestion = answerRepository.findByIdAndDeletedFalse(idAnswer);

        Question questionToAssociate = questionRepository.findByIdAndDeletedFalse(idQuestion);
        if(answerToAssociateQuestion == null){
            throw new ResourceNotFoundException("Answer does not exist");
        }
        if(questionToAssociate == null){
            throw new ResourceNotFoundException("Question does not exist");
        }

        if(answerToAssociateQuestion.getQuestion() != null && answerToAssociateQuestion.getQuestion().getId() == idQuestion){
            throw new DuplicateException("The answer has already been associated with the question '"+ questionToAssociate.getTextQuestion()+ "'.");
        }


        answerToAssociateQuestion.setQuestion(questionToAssociate);

        return answerRepository.save(answerToAssociateQuestion);
    }
}
