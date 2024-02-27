package com.fincons.service.abilityuser;

import com.fincons.dto.AbilityUserDto;
import com.fincons.entity.AbilityUser;
import com.fincons.mapper.AbilityUserMapper;
import com.fincons.repository.AbilityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbilityUserService implements IAbilityUserService{

    @Autowired
    private AbilityUserRepository abilityUserRepository;

    @Autowired
    private AbilityUserMapper abilityUserMapper;

    @Override
    public List<AbilityUser> getAllAbilityUser() {
        return abilityUserRepository.findAll();
    }

    @Override
    public AbilityUser addAbilityUser(AbilityUserDto abilityUserDto) {
        AbilityUser abilityUserSaved = abilityUserRepository.save(abilityUserMapper.mapDtoToAbilityUser(abilityUserDto)) ;
        return abilityUserSaved;
    }
}
