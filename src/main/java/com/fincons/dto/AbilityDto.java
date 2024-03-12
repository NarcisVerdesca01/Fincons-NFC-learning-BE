package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(scope = AbilityDto.class,  generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class AbilityDto {

    private long id;
    private String name;
    private List<AbilityCourseDto> courses;
    private List<AbilityUserDto> users;
    private boolean deleted;


}
