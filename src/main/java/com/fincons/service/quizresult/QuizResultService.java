package com.fincons.service.quizresult;


import com.fincons.dto.QuizResultsDto;
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


        // Inizializza variabili per calcolare il punteggio totale e il punteggio raggiunto
        int risultato = 0;
        int totale = 0;

        // Itera su ogni domanda del quiz
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            Question domanda = quiz.getQuestions().get(i);

            // Incrementa il punteggio totale aggiungendo il punteggio della domanda corrente
            totale += domanda.getValueOfQuestion();

            // Controlla se l'indice della risposta data dallo studente è uguale all'indice della risposta corretta
            if (domanda.getCorrectAnswer() == listAnswers.get(i)) {
                risultato += domanda.getValueOfQuestion(); // Incrementa il punteggio raggiunto
            }
        }

        // Calcola la percentuale di punteggio raggiunto rispetto al punteggio totale
        double percentualeRisultato = (double) risultato / totale * 100;

        // Crea un oggetto QuizResultsDto per memorizzare i risultati
        QuizResults quizResult = new QuizResults();
        quizResult.setUser(user); // Imposta l'utente
        quizResult.setQuiz(quiz); // Imposta il quiz
        quizResult.setTotalScore(percentualeRisultato); // Imposta il punteggio totale del quiz

        // Salva i risultati nel repository dei risultati del quiz
        QuizResults  quizResults = quizResultRepository.save(quizResult);

        return quizResults; // Restituisce i risultati salvati
    }


}
