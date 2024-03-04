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
@JsonIdentityInfo(scope = QuizResultsDto.class, generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class QuizResultsDto {

    private long id;

    private UserDto user;

    private QuizDto quiz;
    private double totalScore;

    // TODO List<Integer> lista di indici delle risposte date

}
