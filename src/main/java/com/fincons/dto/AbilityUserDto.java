package com.fincons.dto;

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
    private UserDto user;
    private AbilityDto ability;

}
