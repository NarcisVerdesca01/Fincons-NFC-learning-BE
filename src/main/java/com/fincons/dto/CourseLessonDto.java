package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseLessonDto {

    private long id;

    @JsonIgnoreProperties("courseLessons")
    private CourseDto course;

    @JsonIgnoreProperties("courseLessons")
    private LessonDto lesson;
    private boolean deleted;

}
