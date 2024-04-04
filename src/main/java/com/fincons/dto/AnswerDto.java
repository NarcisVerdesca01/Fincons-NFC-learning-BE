package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto {

    private long id;

    private String text;

    @JsonIgnoreProperties("answers")
    private QuestionDto question;

    private boolean isCorrect;

    private boolean deleted;

}
