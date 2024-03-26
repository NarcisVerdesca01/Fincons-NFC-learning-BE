package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class QuizResponseDto {

    private long id;
    private QuizDto quiz;
    private QuestionDto question;
    private UserDto user;
    private List<String> chosenAnswers;
    private float scoreOfStudentForQuestion;
    private boolean deleted;


}
