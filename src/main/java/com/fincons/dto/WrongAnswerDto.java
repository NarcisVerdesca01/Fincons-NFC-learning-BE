package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WrongAnswerDto {

    private long id;


    private String text;

    @JsonIgnoreProperties("wrongAnswer")
    @ManyToOne
    private QuestionDto question;

}
