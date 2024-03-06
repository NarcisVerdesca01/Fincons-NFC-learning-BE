package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
@JsonIdentityInfo(scope = QuestionDto.class, generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class QuestionDto {

    private long id;
    private String textQuestion;
    private List<AnswerDto> answers;
    private QuizDto quiz;
    private int valueOfQuestion;

}
