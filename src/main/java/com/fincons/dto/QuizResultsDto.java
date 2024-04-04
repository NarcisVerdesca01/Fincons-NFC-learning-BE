package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizResultsDto {

    private long id;

    @JsonIgnoreProperties("quizResults")
    private UserDto user;

    @JsonIgnoreProperties("quizResults")
    private QuizDto quiz;

    private float totalScore;

    private LocalDate whenDone;

    private boolean deleted;

}
