package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class AbilityDto {

    private long id;

    private String name;

    //@JsonIgnoreProperties("abilities")
    @JsonIgnore
    private List<UserDto> users;

    //@JsonIgnoreProperties("abilities")
    @JsonIgnore
    private List<CourseDto> courses;
}
