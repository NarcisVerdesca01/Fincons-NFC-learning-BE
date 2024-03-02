package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @JsonIgnore
    private List<AbilityCourseDto> courses;

    @JsonIgnore
    private List<AbilityUserDto> users;


}
