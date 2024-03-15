package com.fincons.service.quizresult;

import com.fincons.entity.Answer;
import com.fincons.entity.Question;
import com.fincons.entity.Quiz;
import com.fincons.entity.QuizResults;
import com.fincons.entity.User;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.AnswerRepository;
import com.fincons.repository.QuestionRepository;
import com.fincons.repository.QuizRepository;
import com.fincons.repository.QuizResultRepository;
import com.fincons.repository.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuizResultService implements IQuizResultService{

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;


    @Override
    public List<QuizResults> findAllResultsQuiz() {
        return quizResultRepository.findAllByDeletedFalse();
    }

    @Override
    public List<QuizResults> findQuizResultAboutSingleStudent() {

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this email doesn't exist");
        }

        if (!userRepository.existsByEmail(loggedUser)) {
            throw new ResourceNotFoundException("User does not exist");
        }

        User user = userRepository.findByEmail(loggedUser);

        List<QuizResults> quizResultsList = quizResultRepository.findAllByDeletedFalse();

        return quizResultsList
                .stream()
                .filter(qr -> qr.getUser().getEmail().equals(user.getEmail()))
                .toList();
    }

    @Override
    public QuizResults getQuizResultsById(long id) {

        if(!quizResultRepository.existsByIdAndDeletedFalse(id)){
            throw new ResourceNotFoundException("The quiz-result-student association does not exist") ;
        }

        return quizResultRepository.findByIdAndDeletedFalse(id);

    }
    @Override
    public boolean checkIfAlreadyDone(long quizId){
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(loggedUser);
        Quiz quiz = quizRepository.findByIdAndDeletedFalse(quizId);
        if(quiz==null){
            throw new ResourceNotFoundException("Quiz does not exist");
        }
        if(quizResultRepository.existsByUserAndQuizAndDeletedFalse(user,quiz)){
           return true;
        }
        return false;
    }

    @Override
    public QuizResults calculateAndSave(long quizId, Map<Long, List<Long>> userAnswers) throws DuplicateException {

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this email doesn't exist");
        }

        if (!userRepository.existsByEmail(loggedUser)) {
            throw new ResourceNotFoundException("User does not exist");
        }

        User user = userRepository.findByEmail(loggedUser);


        Quiz quiz = quizRepository.findByIdAndDeletedFalse(quizId);
        if(!quizRepository.existsByIdAndDeletedFalse(quiz.getId())){
            throw new ResourceNotFoundException("Quiz does not exist");
        }

        if(quizResultRepository.existsByUserAndQuizAndDeletedFalse(user,quiz)){
            throw new DuplicateException("The user has already completed the Quiz! Go To Update quiz-results page");
        }

        float total = 0;
        float score = 0;

        for (Map.Entry<Long, List<Long>> entry : userAnswers.entrySet()) {

            Long questionId = entry.getKey();
            List<Long> userAnswerIndices = entry.getValue();

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

            userAnswerIndices.forEach(a-> answerRepository.findById(a).orElseThrow(() -> new ResourceNotFoundException("Answer not found ")));

            total += question.getValueOfQuestion(); // Aumenta il conteggio delle domande

            // Ottieni le risposte corrette associate alla domanda
            List<Answer> correctAnswersOfQuestion = question.getAnswers()
                    .stream()
                    .filter(Answer::isCorrect)
                    .toList();


            // Conta le risposte corrette date dall'utente
            long correctUserAnswersCount = correctAnswersOfQuestion
                    .stream()
                    .filter(answer -> userAnswerIndices.contains(answer.getId())).count();



            // Calcola il punteggio parziale in base alle risposte corrette date dall'utente e le risposte corrette totali
            double partialScore = ((double) correctUserAnswersCount / (double) correctAnswersOfQuestion.size()) *  question.getValueOfQuestion();

            score +=  partialScore;

        }

        float percentageScore = ( score / total) * 100;
        QuizResults quizResult = new QuizResults();
        quizResult.setUser(user);
        quizResult.setQuiz(quiz);
        quizResult.setTotalScore( percentageScore);
       float percentuale =  quizResult.getTotalScore();
          QuizResults savedEntity= quizResultRepository.save(quizResult);
        return savedEntity;
    }

    @Override
    public QuizResults redoQuiz(long quizIdToModify, Map<Long, List<Long>> userAnswers) {

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();

        /*
                if(!quizResultRepository.existsByIdAndDeletedFalse(quizResultsToModify)){
            throw new ResourceNotFoundException("The user has never complete the quiz before please go to do quiz for the first time");
        }
         */


        if (loggedUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this email doesn't exist");
        }

        if (!userRepository.existsByEmail(loggedUser)) {
            throw new ResourceNotFoundException("User does not exist");
        }

        User user = userRepository.findByEmail(loggedUser);

        Quiz quiz = quizRepository.findByIdAndDeletedFalse(quizIdToModify);


        //appena agigunto
        if(quizResultRepository.findByUserAndQuizAndDeletedFalse(user,quiz)==null){
            throw new ResourceNotFoundException("User-Quiz association does not exist");
        }


        if(!quizRepository.existsByIdAndDeletedFalse(quiz.getId())){
            throw new ResourceNotFoundException("Quiz does not exist");
        }


        float total = 0;
        float score = 0;

        for (Map.Entry<Long, List<Long>> entry : userAnswers.entrySet()) {

            Long questionId = entry.getKey();
            List<Long> userAnswerIndices = entry.getValue();

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

            userAnswerIndices.forEach(a-> answerRepository.findById(a).orElseThrow(() -> new ResourceNotFoundException("Answer not found ")));

            total += question.getValueOfQuestion();

            List<Answer> correctAnswersOfQuestion = question.getAnswers()
                    .stream()
                    .filter(Answer::isCorrect)
                    .toList();


            long correctUserAnswersCount = correctAnswersOfQuestion
                    .stream()
                    .filter(answer -> userAnswerIndices.contains(answer.getId())).count();

            double partialScore = ((double) correctUserAnswersCount / (double) correctAnswersOfQuestion.size()) *  question.getValueOfQuestion();

            score +=  partialScore;

        }

        float percentageScore = ( score / total) * 100;
        QuizResults quizResultsToModify =  quizResultRepository.findByUserAndQuizAndDeletedFalse(user,quiz);
        quizResultsToModify.setUser(user);
        quizResultsToModify.setQuiz(quiz);
        quizResultsToModify.setTotalScore( percentageScore);
        float percentuale =  quizResultsToModify.getTotalScore();
        QuizResults savedEntity= quizResultRepository.save(quizResultsToModify);
        return savedEntity;
    }




}
