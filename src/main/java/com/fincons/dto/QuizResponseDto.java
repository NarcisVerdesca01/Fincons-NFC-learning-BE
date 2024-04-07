package com.fincons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponseDto {

    private long id;

    private QuizDto quiz;

    private QuestionDto question;

    private UserDto user;

    private List<String> chosenAnswers;

    private float scoreOfStudentForQuestion;

    private boolean deleted;


}
