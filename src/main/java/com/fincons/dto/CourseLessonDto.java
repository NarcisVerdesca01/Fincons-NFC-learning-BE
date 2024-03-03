package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    private CourseDto course;

    @JsonBackReference
    private LessonDto lesson;
}
