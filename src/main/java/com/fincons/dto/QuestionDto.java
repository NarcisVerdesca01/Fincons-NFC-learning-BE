package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;;
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
    @JsonIgnoreProperties("question")
    private List<AnswerDto> answers;
    @JsonIgnoreProperties("question")
    private QuizDto quiz;
    private int valueOfQuestion;
    private boolean deleted;


}
