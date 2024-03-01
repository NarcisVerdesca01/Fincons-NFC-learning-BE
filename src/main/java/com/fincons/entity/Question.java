package com.fincons.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text", length = 20971520)
    private String textQuestion;

    @Column(name = "correctAnswer")
    private int correctAnswer;

    //5. DOMANDA - RISPOSTE (Question.class - Answer.class) 1:N   la domanda a più rispostesbagliate
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL) //
    private Answer[] answer;

    //4. QUIZ - DOMANDE(question.class) 1:N Un quiz a molte domande
    @ManyToOne
    private Quiz quiz;

    @Column(name = "score")
    private int score;


}
