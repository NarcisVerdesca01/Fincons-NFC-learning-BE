package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
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
