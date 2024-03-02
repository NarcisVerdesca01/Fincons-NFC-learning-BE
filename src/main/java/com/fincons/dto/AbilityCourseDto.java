package com.fincons.dto;

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
    private CourseDto course;
    private AbilityDto ability;

}
