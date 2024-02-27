package com.fincons.service.ability;

import com.fincons.dto.AbilityDto;
import com.fincons.entity.Ability;
import com.fincons.exception.AbilityException;
import com.fincons.jwt.JwtTokenProvider;
import com.fincons.mapper.AbilityMapper;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.RoleRepository;
import com.fincons.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbilityService implements IAbilityService{

    private AbilityMapper abilityMapper;

    private AbilityRepository abilityRepository;

    public AbilityService(AbilityRepository abilityRepository, AbilityMapper abilityMapper) {
        this.abilityRepository = abilityRepository;
        this.abilityMapper = abilityMapper;
    }


    @Override
    public List<Ability> findAllAbilities() {
        return abilityRepository.findAll();
    }

    @Override
    public Ability findAbilityByName(String name) throws AbilityException {
        Ability ability = abilityRepository.findByName(name);
        if(ability==null || name.isEmpty()){
           throw new AbilityException(AbilityException.abilityDosNotExist());
        }
        return ability;
    }

    @Override
    public Ability createAbility(AbilityDto abilityDto) throws AbilityException {
        if(abilityDto.getName().isBlank()){
            throw new AbilityException("The name of ability can't be empty");
        }
        if(abilityRepository.existsByName(abilityDto.getName())){
            throw new AbilityException("The name of ability already exists");
        }
        Ability abilityToSave = abilityMapper.mapDtoToAbility(abilityDto);
        return abilityRepository.save(abilityToSave);
    }
}
