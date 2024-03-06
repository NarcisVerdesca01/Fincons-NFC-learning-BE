package com.fincons.service.quiz;


import com.fincons.dto.QuizDto;
import com.fincons.entity.Lesson;
import com.fincons.entity.Question;
import com.fincons.entity.Quiz;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.LessonRepository;
import com.fincons.repository.QuestionRepository;
import com.fincons.repository.QuizRepository;
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
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("The quiz does not exist!");
        }
        return quizRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("The quiz does not exist."));
    }

    @Override
    public List<Quiz> findAllQuiz() {
        return quizRepository.findAll();
    }

    @Override
    public Quiz createQuiz(QuizDto quizDto) throws DuplicateException {
        if (quizRepository.existsByTitle(quizDto.getTitle())) {
            throw new DuplicateException("Quiz already exists");
        }

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

    @Override
    public Quiz associateLesson(long idQuiz, long idLesson)  throws DuplicateException {
        Quiz quizToAssociate = quizRepository.findById(idQuiz)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz does not exist. "));

        Lesson lessonToAssociate = lessonRepository.findById(idLesson)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson does not exist. "));


        if(quizToAssociate.getLesson()!= null && quizToAssociate.getLesson().getId()== lessonToAssociate.getId()){
            throw new DuplicateException("The lesson is already associated with the quiz");
        }


        boolean existsLesson = quizRepository.existsByLesson(lessonToAssociate);
        if(existsLesson){
            throw new DuplicateException("The lesson is already associated with another quiz");
        }

        quizToAssociate.setLesson(lessonToAssociate);


        return quizRepository.save(quizToAssociate);
    }

    @Override
    public Quiz associateQuestion(long idQuiz, long idQuestion) throws DuplicateException {
        Quiz quizToAssociate = quizRepository.findById(idQuiz)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz does not exist. "));

        Question questionToAssociate = questionRepository.findById(idQuestion)
                .orElseThrow(() -> new ResourceNotFoundException("Question does not exist. "));


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
