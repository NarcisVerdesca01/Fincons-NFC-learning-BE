package com.fincons.service.abilityuser;

import com.fincons.dto.AbilityUserDto;
import com.fincons.entity.AbilityUser;
import com.fincons.exception.DuplicateException;
import java.util.List;

public interface IAbilityUserService {
    List<AbilityUser> getAllAbilityUser();

    AbilityUser addAbilityUser(AbilityUserDto abilityUserDto) throws DuplicateException;

    AbilityUser getAbilityUserById(long id);

    AbilityUser updateAbilityUser(long id, AbilityUserDto abilityUserDto) throws DuplicateException;

    void deleteAbilityUser(long id);
}
