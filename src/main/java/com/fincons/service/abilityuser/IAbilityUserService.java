package com.fincons.service.abilityuser;

import com.fincons.dto.AbilityUserDto;
import com.fincons.entity.AbilityUser;

import java.util.List;

public interface IAbilityUserService {
    List<AbilityUser> getAllAbilityUser();

    AbilityUser addAbilityUser(AbilityUserDto abilityUserDto);
}
