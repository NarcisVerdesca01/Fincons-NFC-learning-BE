package com.fincons.dto;

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
    private CourseDto course;
    private LessonDto lesson;
}
