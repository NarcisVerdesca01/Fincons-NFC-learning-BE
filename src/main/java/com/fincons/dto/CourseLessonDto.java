package com.fincons.dto;

import com.fincons.entity.Course;
import com.fincons.entity.Lesson;
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
    private Course course;
    private Lesson lesson;
}
