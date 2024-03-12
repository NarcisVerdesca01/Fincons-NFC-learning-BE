package com.fincons.service.ability;

import com.fincons.dto.AbilityDto;
import com.fincons.entity.Ability;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityMapper;
import com.fincons.repository.AbilityRepository;
import io.micrometer.common.util.StringUtils;
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
        return abilityRepository.findAllByDeletedFalse();
    }

    @Override
    public Ability findAbilityByName(String name)  {
        Ability ability = abilityRepository.findByNameAndDeletedFalse(name);
        if(ability==null || name.isEmpty()){
           throw new ResourceNotFoundException("Ability " + name + " does not exists!");
        }
        return ability;
    }

    @Override
    public Ability createAbility(AbilityDto abilityDto) throws DuplicateException {

        if (StringUtils.isBlank(abilityDto.getName())) {
            throw new IllegalArgumentException("The name of ability can't be blank");
        }

        if (abilityRepository.existsByNameAndDeletedFalse(abilityDto.getName())) {
            throw new DuplicateException("The name of ability already exists");
        }
        Ability ability = new Ability();
        ability.setName(abilityDto.getName());
        return abilityRepository.save(ability);
    }


    @Override
    public Ability updateAbility(long id, AbilityDto abilityDto) throws DuplicateException {

        Ability abilityToModify = abilityRepository.findByIdAndDeletedFalse(id);
        if(abilityToModify == null){
            throw new ResourceNotFoundException("Ability does not exist");
        }

        if(abilityDto.getName() != null){
            if(!abilityRepository.existsByNameAndIdNot(abilityDto.getName(),abilityToModify.getId())){
                abilityToModify.setName(abilityDto.getName());
            }else{
                throw new DuplicateException("Ability already exists");
            }
        } else {
            throw new IllegalArgumentException("Ability name cannot be null");
        }
        return abilityRepository.save(abilityToModify);

    }

    @Override
    public void deleteAbility(long id) {
        validateAbilityById(id);
        Ability abilityToDelete = abilityRepository.findByIdAndDeletedFalse(id);
        abilityToDelete.setDeleted(true);
        abilityRepository.save(abilityToDelete);
    }

    private void validateAbilityById(long id) {
        if (!abilityRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The ability does not exist");
        }
    }

    @Override
    public Ability findAbilityById(long id) {
        if (!abilityRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The ability does not exist!");
        }
        return abilityRepository.findByIdAndDeletedFalse(id);
    }
}
