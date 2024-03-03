package com.fincons.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference
    private Answer[] answers;

    //4. QUIZ - DOMANDE(question.class) 1:N Un quiz a molte domande
    @ManyToOne
    @JsonBackReference
    private Quiz quiz;

    @Column(name = "valueOfQuestion")
    private int valueOfQuestion;


}
