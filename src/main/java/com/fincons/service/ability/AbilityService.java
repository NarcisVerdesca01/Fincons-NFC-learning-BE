package com.fincons.service.ability;

import com.fincons.entity.Ability;
import com.fincons.jwt.JwtTokenProvider;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.RoleRepository;
import com.fincons.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
