package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class QuizResultsDto {

    private long id;

    @JsonIgnoreProperties("quizResults")
    //@JsonBackReference
    private UserDto user;

    @JsonIgnoreProperties("quizResults")
    //@JsonBackReference
    private QuizDto quiz;

    private float totalScore;

    private LocalDate whenDone;

    private boolean deleted;

}
