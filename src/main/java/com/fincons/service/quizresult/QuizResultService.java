package com.fincons.service.quizresult;

import com.fincons.entity.Question;
import com.fincons.entity.Quiz;
import com.fincons.entity.QuizResults;
import com.fincons.entity.User;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.QuizRepository;
import com.fincons.repository.QuizResultRepository;
import com.fincons.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class QuizResultService implements IQuizResultService{

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;


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
    public QuizResults calculateAndSave(long quizId, String userEmail, List<Integer> listAnswers) {

        User user = userRepository.findByEmail(userEmail);

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(()-> new ResourceNotFoundException("Quiz does not exist"));

        /*
                //Initialise variables to calculate total score and achieved score
        int risultato = 0;
        int totale = 0;


        // Iteration on each quiz question
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            Question domanda = quiz.getQuestions().get(i);

        // Increases the total score by adding the score of the current question
            totale += domanda.getValueOfQuestion();

            // Checks whether the index of the answer given by the student is equal to the index of the correct answer
            if (domanda.getCorrectAnswer() == listAnswers.get(i)) {
                risultato += domanda.getValueOfQuestion(); // Increase result
            }
        }
         */

        int total = quiz.getQuestions()
                .stream()
                .map(Question::getValueOfQuestion)
                .mapToInt(Integer::intValue)
                .sum();

        int result = IntStream.range(0, quiz.getQuestions().size())
                .filter(i -> quiz.getQuestions().get(i).getCorrectAnswer() == listAnswers.get(i))
                .mapToObj(i -> quiz.getQuestions().get(i))
                .mapToInt(Question::getValueOfQuestion)
                .sum();



        // Calculates the percentage of score achieved in relation to the total score
        double percentualeRisultato = (double) result / total * 100;

        // Create a QuizResultsDto object to store results
        QuizResults quizResult = new QuizResults();
        quizResult.setUser(user);
        quizResult.setQuiz(quiz);
        quizResult.setTotalScore(percentualeRisultato);

        QuizResults  quizResults = quizResultRepository.save(quizResult);

        return quizResults;
    }





}
