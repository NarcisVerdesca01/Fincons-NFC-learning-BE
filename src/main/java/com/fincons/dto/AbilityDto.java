package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class AbilityDto {

    private long id;
    private String name;

    @JsonIgnoreProperties("ability")
    private List<AbilityCourseDto> abilityCourses;

    @JsonIgnoreProperties("ability")
    private List<AbilityUserDto> abilityUsers;

    private boolean deleted;


}
