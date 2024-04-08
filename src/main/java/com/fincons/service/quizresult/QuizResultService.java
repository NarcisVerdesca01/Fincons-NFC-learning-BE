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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
        checkEmpty(loggedUser);

        validateExistence(loggedUser);

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
        checkEmpty(loggedUser);
        validateExistence(loggedUser);

        User user = userRepository.findByEmail(loggedUser);
        Quiz quiz = quizRepository.findByIdAndDeletedFalse(quizId);
        validateExistenceQuiz(quiz);
        validateUserQuizAssociation(user, quiz);

        float score = calculateScoreAndSaveQuizResponces(userAnswers);

        float total = quiz.getQuestions()
                .stream()
                .map(Question::getValueOfQuestion)
                .reduce(0, Integer::sum);

        float percentageScore = ( score / total) * 100;
        QuizResults quizResult = new QuizResults();
        quizResult.setUser(user);
        quizResult.setQuiz(quiz);
        quizResult.setWhenDone(LocalDate.now());
        quizResult.setTotalScore( percentageScore);
        quizResult.setDeleted(false);
        return quizResultRepository.save(quizResult);
    }


    @Override
    public QuizResults redoQuiz(long quizIdToModify, Map<Long, List<Long>> userAnswers) {
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        checkEmpty(loggedUser);
        validateExistence(loggedUser);

        User user = userRepository.findByEmail(loggedUser);
        Quiz quiz = quizRepository.findByIdAndDeletedFalse(quizIdToModify);
        validateExistenceQuiz(quiz);
        validateByUserQuizAssociation(user, quiz);

        float score = calculateScoreAndSaveQuizResponces(userAnswers);

        float total = quiz.getQuestions()
                .stream()
                .map(Question::getValueOfQuestion)
                .reduce(0, Integer::sum);

        float percentageScore = (score / total) * 100;

        QuizResults quizResultsToModify =  quizResultRepository.findByUserAndQuizAndDeletedFalse(user, quiz);
        quizResultsToModify.setUser(user);
        quizResultsToModify.setQuiz(quiz);
        quizResultsToModify.setTotalScore(percentageScore);
        quizResultsToModify.setWhenDone(LocalDate.now());

        return quizResultRepository.save(quizResultsToModify);
    }

    private float calculateScoreAndSaveQuizResponces(Map<Long, List<Long>> userAnswers) {

        float score = 0;

        for (Map.Entry<Long, List<Long>> entry : userAnswers.entrySet()) {
            Long questionId = entry.getKey();
            List<Long> userAnswerIndices = entry.getValue();

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

            userAnswerIndices.forEach(a-> answerRepository.findById(a).orElseThrow(() -> new ResourceNotFoundException("Answer not found ")));

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
        return score;
    }

    private void validateByUserQuizAssociation(User user, Quiz quiz) {
        if(quizResultRepository.findByUserAndQuizAndDeletedFalse(user, quiz)==null){
            throw new ResourceNotFoundException("User-Quiz association does not exist");
        }
    }

    private void validateUserQuizAssociation(User user, Quiz quiz) throws DuplicateException {
        if(quizResultRepository.existsByUserAndQuizAndDeletedFalse(user, quiz)){
            throw new DuplicateException("The user has already completed the Quiz! Go To Update quiz-results page");
        }
    }

    private void validateExistenceQuiz(Quiz quiz) {
        if(!quizRepository.existsByIdAndDeletedFalse(quiz.getId())){
            throw new ResourceNotFoundException("Quiz does not exist");
        }
    }

    private void validateExistence(String loggedUser) {
        if (!userRepository.existsByEmail(loggedUser)) {
            throw new ResourceNotFoundException("User does not exist");
        }
    }

    private static void checkEmpty(String loggedUser) {
        if (loggedUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this email doesn't exist");
        }
    }


}
