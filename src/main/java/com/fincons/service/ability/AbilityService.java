package com.fincons.service.ability;

import com.fincons.dto.AbilityDto;
import com.fincons.entity.Ability;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityMapper;
import com.fincons.repository.AbilityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

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
    public Ability findAbilityByName(String name)  {
        Ability ability = abilityRepository.findByName(name);
        if(ability==null || name.isEmpty()){
           throw new ResourceNotFoundException("Ability " + name + " does not exists!");
        }
        return ability;
    }

    @Override
    public Ability createAbility(AbilityDto abilityDto) throws DuplicateException {
        if(abilityDto.getName().isBlank()){
            throw new IllegalArgumentException("The name of ability can't be empty");
        }
        if(abilityRepository.existsByNameIgnoreCase(abilityDto.getName())){
            throw new DuplicateException("The name of ability already exists");
        }
        Ability abilityToSave = abilityMapper.mapDtoToAbility(abilityDto);
        return abilityRepository.save(abilityToSave);
    }

    @Override
    public Ability updateAbility(long id, AbilityDto abilityDto) throws DuplicateException {

        Ability abilityToModify = abilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ability does not exist"));

        if (abilityDto.getName() != null && !abilityRepository.existsByName(abilityDto.getName())) {
            abilityToModify.setName(abilityDto.getName());
        }else if(abilityRepository.existsByName(abilityDto.getName())){
            throw new DuplicateException("Ability already exists!");
        }

        return abilityRepository.save(abilityToModify);

    }

    @Override
    public void deleteAbility(long id) {
        if (!abilityRepository.existsById(id)) {
            throw new ResourceNotFoundException("The ability does not exist");
        }

        abilityRepository.deleteById(id);
    }

    @Override
    public Ability findAbilityById(long id) {
        if (!abilityRepository.existsById(id)) {
            throw new ResourceNotFoundException("The ability does not exist!");
        }
        return abilityRepository.findById(id).orElse(null);
    }
}
