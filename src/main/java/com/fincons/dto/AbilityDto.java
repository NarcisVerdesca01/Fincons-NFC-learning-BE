package com.fincons.dto;

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

    private String nameOfAbility;

    private List<UserDto> users;

    private List<CourseDto> courses;
}
