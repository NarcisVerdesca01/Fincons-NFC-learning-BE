package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(scope = CourseLessonDto.class, generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class CourseLessonDto {

    private long id;
    private CourseDto course;
    private LessonDto lesson;
}
