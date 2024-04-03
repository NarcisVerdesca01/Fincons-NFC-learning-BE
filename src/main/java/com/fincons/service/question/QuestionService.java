package com.fincons.service.question;

import com.fincons.dto.QuestionDto;
import com.fincons.entity.Answer;
import com.fincons.entity.Question;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.AnswerRepository;
import com.fincons.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService implements  IQuestionService{

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

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
    public List<Question> findAllQuestionWithoutQuiz() {
        return questionRepository.findAllByDeletedFalseAndQuizIsNull();
    }

    @Override
    public List<Question> findAllQuestionWithoutAnswers() {
        return questionRepository.findAllByDeletedFalseAndAnswersIsNull();
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
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The question does not exist");
        }
        Question questionToDelete = questionRepository.findByIdAndDeletedFalse(id);
        questionToDelete.setDeleted(true);
        questionRepository.save(questionToDelete);

        List<Answer> answersToSetQuestionNull = answerRepository.findAllByDeletedFalse()
                .stream()
                .filter(answer-> id.equals(Optional.of(answer).map(Answer::getQuestion).map(Question::getId).orElse(null)))
                .toList();

        answersToSetQuestionNull
                .forEach(answer->answer.setQuestion(null));

        answersToSetQuestionNull
                .forEach(answer -> answerRepository.save(answer));

    }

    @Override
    public Question updateQuestion(long id, QuestionDto questionDto) {
        Question questionToModify = questionRepository.findByIdAndDeletedFalse(id);

        if(questionToModify == null ){
            throw new ResourceNotFoundException("Question does not exist");
        }

        if (questionDto.getTextQuestion() == null) {
            throw new IllegalArgumentException("Text or score of question is null");
        }

        if(questionToModify.getValueOfQuestion() != questionDto.getValueOfQuestion() && questionDto.getValueOfQuestion()>0){
            questionToModify.setValueOfQuestion(questionDto.getValueOfQuestion());
        }
        questionToModify.setTextQuestion(questionDto.getTextQuestion());

        return questionRepository.save(questionToModify);
    }


}
