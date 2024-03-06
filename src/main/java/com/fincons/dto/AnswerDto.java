package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fincons.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo( scope = AnswerDto.class ,generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class AnswerDto {

    private long id;
    private String text;
    private QuestionDto question;
    private boolean isCorrect;
}
