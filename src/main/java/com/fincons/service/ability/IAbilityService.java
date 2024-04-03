package com.fincons.service.ability;

import com.fincons.dto.AbilityDto;
import com.fincons.entity.Ability;
import com.fincons.exception.DuplicateException;
import java.util.List;

public interface IAbilityService {

    List<Ability> findAllAbilities();

    Ability findAbilityByName(String name);

    Ability createAbility(AbilityDto abilityDto) throws DuplicateException;

    Ability updateAbility(long id, AbilityDto abilityDto) throws DuplicateException;

    void deleteAbility(long id);

    Ability findAbilityById(long id);


}
