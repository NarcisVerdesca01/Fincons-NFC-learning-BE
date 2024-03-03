package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    private UserDto user;

    @JsonBackReference
    private AbilityDto ability;

}
