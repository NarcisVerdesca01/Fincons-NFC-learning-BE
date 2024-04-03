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
public class AbilityCourseDto {

    private long id;

    @JsonIgnoreProperties("abilityCourses")
    private CourseDto course;

    @JsonIgnoreProperties("abilityCourses")
    private AbilityDto ability;

    private boolean deleted;

}
