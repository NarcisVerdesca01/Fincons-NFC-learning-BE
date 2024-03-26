package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AbilityUserDto {

    private long id;
    @JsonIgnoreProperties("abilityUsers")
    private UserDto user;
    @JsonIgnoreProperties("abilityUsers")
    private AbilityDto ability;
    private boolean deleted;




}
