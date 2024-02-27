package com.fincons.dto;

import com.fincons.entity.Ability;
import com.fincons.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbilityUserDto {

    private long id;
    private UserDto userDto;
    private AbilityDto abilityDto;

}
