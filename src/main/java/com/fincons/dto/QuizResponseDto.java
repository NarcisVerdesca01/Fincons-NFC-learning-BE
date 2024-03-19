package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fincons.entity.Question;
import com.fincons.entity.QuizResults;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(scope = QuizDto.class,  generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class QuizResponseDto {

    private long id;
    private QuizResults quizResult;
    private Question question;
    private List<String> chosenAnswers;
    private float scoreOfStudentForQuestion;

}
