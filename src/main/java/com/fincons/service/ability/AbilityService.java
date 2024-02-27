package com.fincons.service.ability;

import com.fincons.entity.Ability;
import com.fincons.exception.AbilityException;
import com.fincons.jwt.JwtTokenProvider;
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

    private AbilityRepository abilityRepository;
    public AbilityService(AbilityRepository abilityRepository) {
        this.abilityRepository = abilityRepository;
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
}
