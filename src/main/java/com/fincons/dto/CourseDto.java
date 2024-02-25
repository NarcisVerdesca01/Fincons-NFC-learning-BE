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
public class CourseDto {

    private long id;

    private String name;

    private String description;

    @JsonIgnoreProperties("courses")
    private List<LessonDto> lessons;

    @JsonIgnoreProperties("courses")
    private List<AbilityDto> abilities;

}
