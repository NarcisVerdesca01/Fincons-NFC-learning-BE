package com.fincons.service.quiz;


import com.fincons.dto.QuizDto;
import com.fincons.entity.Ability;
import com.fincons.entity.Lesson;
import com.fincons.entity.Question;
import com.fincons.entity.Quiz;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.LessonRepository;
import com.fincons.repository.QuestionRepository;
import com.fincons.repository.QuizRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuizService implements IQuizService{

      @Autowired
      private QuizRepository quizRepository;
      @Autowired
      private LessonRepository lessonRepository;
      @Autowired
      private QuestionRepository questionRepository;

    @Override
    public Quiz findById(long id) {
        if (!quizRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The quiz does not exist!");
        }
        return quizRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<Quiz> findAllQuiz() {
        return quizRepository.findAllByDeletedFalse();
    }

    @Override
    public Quiz createQuiz(QuizDto quizDto) throws DuplicateException {

        if (StringUtils.isBlank(quizDto.getTitle())) {
            throw new IllegalArgumentException("The name of quiz can't be blank");
        }

        if (quizRepository.existsByTitleAndDeletedFalse(quizDto.getTitle())) {
            throw new DuplicateException("Quiz already exists");
        }

        Quiz newQuiz= new Quiz();
        newQuiz.setTitle(quizDto.getTitle());
        Quiz savedQuiz= quizRepository.save(newQuiz);

        return savedQuiz;
    }

    @Override
    public void deleteQuiz(long id) {
        if (!quizRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The quiz does not exist");
        }

        Quiz quizToDelete = quizRepository.findByIdAndDeletedFalse(id);
        quizToDelete.setDeleted(true);
        quizRepository.save(quizToDelete);

        Lesson lesson = lessonRepository.findByQuiz(quizToDelete);
        if (lesson != null) {
            lesson.setQuiz(null);
            lessonRepository.save(lesson);
        }

        List<Question> questionsToSetQuizNull = questionRepository.findAllByDeletedFalse()
                .stream()
                .filter(question->question.getQuiz().getId()==id)
                .toList();

        questionsToSetQuizNull
                .forEach(question->question.setQuiz(null));

        questionsToSetQuizNull
                .forEach(question -> questionRepository.save(question));

    }

    @Override
    public Quiz updateQuiz(long id, QuizDto quizDto) {
        Quiz quizToModify = quizRepository.findByIdAndDeletedFalse(id);;
        if(quizToModify == null){
            throw new ResourceNotFoundException("Quiz does not exist");
        }
        if (quizDto.getTitle() == null) {
            throw new IllegalArgumentException("Title of quiz is null");
            //TODO-IMPLEMENTARE L'AGGIORNAMENTO DEL TYPE
        }
        quizToModify.setTitle(quizDto.getTitle());
        return quizRepository.save(quizToModify);
    }

    @Override
    public Quiz associateLesson(long idQuiz, long idLesson)  throws DuplicateException {

        Quiz existingQuiz = quizRepository.findByIdAndDeletedFalse(idQuiz);

        Lesson existingLesson = lessonRepository.findByIdAndDeletedFalse(idLesson);

        if(existingQuiz == null){
            throw new ResourceNotFoundException("Quiz does not exist");
        }
        if(existingLesson == null){
            throw new ResourceNotFoundException("Lesson does not exist");
        }

        if(existingQuiz.getLesson() != null && existingQuiz.getLesson().getId() == existingLesson.getId()){
            throw new DuplicateException("The lesson is already associated with the quiz");
        }

        boolean existsLesson = quizRepository.existsByLesson(existingLesson);
        if(existsLesson){
            throw new DuplicateException("The lesson is already associated with another quiz");
        }

        existingQuiz.setLesson(existingLesson);
        existingLesson.setQuiz(existingQuiz);

        return quizRepository.save(existingQuiz);
    }

    @Override
    public Quiz associateQuestion(long idQuiz, long idQuestion) throws DuplicateException {
        Quiz quizToAssociate = quizRepository.findByIdAndDeletedFalse(idQuiz);

        Question questionToAssociate = questionRepository.findByIdAndDeletedFalse(idQuestion);

        if(quizToAssociate == null){
            throw new ResourceNotFoundException("Quiz does not exist");
        }
        if(questionToAssociate == null){
            throw new ResourceNotFoundException("Question does not exist");
        }

        if(questionToAssociate.getQuiz()!= null){
            throw new DuplicateException("The question is already associated with a quiz");
        }

        if(questionToAssociate.getQuiz()!= null && questionToAssociate.getQuiz().getId()== quizToAssociate.getId()){
            throw new DuplicateException("The question is already associated with the quiz");
        }


        questionToAssociate.setQuiz(quizToAssociate);

        return quizRepository.save(quizToAssociate);
    }
}
