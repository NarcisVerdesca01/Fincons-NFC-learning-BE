package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {


    private long id;

    private String textQuestion;

    private String correctAnswer;

    @JsonIgnoreProperties("question")
    private List<WrongAnswerDto> wrongAnswer;

    @JsonIgnoreProperties("questions")
    private QuizDto quiz;

}
