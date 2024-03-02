package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fincons.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {


    private long id;

    private String textQuestion;

    private int correctAnswer;

    @JsonIgnoreProperties("question")
    private Answer[] answer;

    @JsonIgnoreProperties("questions")
    private QuizDto quiz;

    private int score;

}
