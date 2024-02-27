package com.fincons.service.ability;

import com.fincons.dto.AbilityDto;
import com.fincons.entity.Ability;
import com.fincons.exception.AbilityException;

import java.util.Arrays;
import java.util.List;

public interface IAbilityService {

    List<Ability> findAllAbilities();

    Ability findAbilityByName(String name) throws AbilityException;

    Ability createAbility(AbilityDto abilityDto) throws AbilityException;
}
