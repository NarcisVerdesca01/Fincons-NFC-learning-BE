package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonDto {

    private long id;

    private String title;

    @JsonIgnoreProperties("lessons")
    private List<CourseDto> courses;

    @JsonIgnoreProperties("lesson")
    private QuizDto quiz;

    @JsonIgnoreProperties("lesson")
    private ContentDto content;

}
