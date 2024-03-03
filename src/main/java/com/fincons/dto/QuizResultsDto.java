package com.fincons.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fincons.entity.Quiz;
import com.fincons.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizResultsDto {

    private long id;

    @JsonBackReference
    private User user;

    @JsonBackReference
    private Quiz quiz;
    private double totalScore;

    // TODO List<Integer> lista di indici delle risposte date

}
