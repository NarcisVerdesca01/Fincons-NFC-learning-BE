package com.fincons.service.quizresult;

import com.fincons.entity.Answer;
import com.fincons.entity.Question;
import com.fincons.entity.Quiz;
import com.fincons.entity.QuizResults;
import com.fincons.entity.User;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.AnswerRepository;
import com.fincons.repository.QuestionRepository;
import com.fincons.repository.QuizRepository;
import com.fincons.repository.QuizResultRepository;
import com.fincons.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return quizResultRepository.findAll();
    }

    @Override
    public QuizResults getQuizResultsById(long id) {
        return quizResultRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("The Quiz-Results-Student association does not exist"));
    }

    @Override
    public QuizResults calculateAndSave(long quizId, String userEmail, Map<Long, List<Long>> userAnswers) {
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + userEmail);
        }

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found with ID: " + quizId));

        int total = 0;
        double score = 0;

        for (Map.Entry<Long, List<Long>> entry : userAnswers.entrySet()) {
            Long questionId = entry.getKey();
            List<Long> userAnswerIndices = entry.getValue();

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

            total += question.getValueOfQuestion(); // Aumenta il conteggio delle domande

            // Ottieni le risposte corrette associate alla domanda
            List<Answer> correctAnswers = question.getAnswers()
                    .stream()
                    .filter(Answer::isCorrect)
                    .toList();

            // Conta le risposte corrette date dall'utente
            long correctUserAnswersCount = correctAnswers
                    .stream()
                    .filter(answer -> userAnswerIndices.contains(answer.getId())).count();

            // Calcola il punteggio parziale in base alle risposte corrette date dall'utente e le risposte corrette totali
            double partialScore = ((double) correctUserAnswersCount / correctAnswers.size()) * question.getValueOfQuestion();

            score +=  partialScore;
        }

        double percentageScore = ( score / total) * 100;

        QuizResults quizResult = new QuizResults();
        quizResult.setUser(user);
        quizResult.setQuiz(quiz);
        quizResult.setTotalScore(percentageScore);

        return quizResultRepository.save(quizResult);
    }



}
