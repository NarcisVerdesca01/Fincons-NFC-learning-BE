package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fincons.entity.AbilityCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbilityDto {

    private long id;

    private String name;

    //@JsonIgnoreProperties("abilities")
    @JsonIgnore
    private List<AbilityUserDto> users;

    //@JsonIgnoreProperties("abilities")
    @JsonIgnore
    private List<AbilityCourseDto> courses;
}
