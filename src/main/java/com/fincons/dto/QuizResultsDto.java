package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
@JsonIdentityInfo(scope = QuizResultsDto.class, generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class QuizResultsDto {

    private long id;
    private UserDto user;
    private QuizDto quiz;
    private float totalScore;
    private LocalDate whenDone;
    private boolean deleted;


}
