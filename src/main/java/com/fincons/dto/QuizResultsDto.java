package com.fincons.dto;


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


    private User user;


    private Quiz quiz;


    private double totalScore;

    // TODO List<Integer> lista di indici delle risposte date

}
