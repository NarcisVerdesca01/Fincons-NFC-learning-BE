package com.fincons.service.ability;

import com.fincons.dto.AbilityDto;
import com.fincons.entity.Ability;
import com.fincons.entity.AbilityCourse;
import com.fincons.entity.AbilityUser;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.AbilityCourseRepository;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.AbilityUserRepository;
import com.fincons.utility.TitleOrDescriptionValidator;
import io.micrometer.common.util.StringUtils;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AbilityService implements IAbilityService{


    private AbilityRepository abilityRepository;

    private AbilityUserRepository abilityUserRepository;

    private AbilityCourseRepository abilityCourseRepository;

    public AbilityService(AbilityRepository abilityRepository, AbilityUserRepository abilityUserRepository, AbilityCourseRepository abilityCourseRepository) {
        this.abilityRepository = abilityRepository;
        this.abilityUserRepository = abilityUserRepository;
        this.abilityCourseRepository = abilityCourseRepository;
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

        checkBlank(abilityDto);
        checkTitleValidity(abilityDto);
        checkNameExistence(abilityDto);

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
            checkTitleValidity(abilityDto);

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

        List<AbilityUser> listOfAbilityUserAssociationsToDelete = abilityUserRepository.findAllByDeletedFalse()
                .stream()
                .filter(au-> au.getAbility().getId()==id)
                .toList();

        List<AbilityCourse> listOfAbilityCourseAssociationToDelete = abilityCourseRepository.findAllByDeletedFalse()
                .stream()
                .filter(ac-> ac.getAbility().getId()==id)
                .toList();



        Ability abilityToDelete = abilityRepository.findByIdAndDeletedFalse(id);
        abilityToDelete.setDeleted(true);

        listOfAbilityUserAssociationsToDelete
                .forEach(au -> au.setDeleted(true));
        listOfAbilityUserAssociationsToDelete
                .forEach(au -> abilityUserRepository.save(au));

        listOfAbilityCourseAssociationToDelete
                .forEach(ac->ac.setDeleted(true));
        listOfAbilityCourseAssociationToDelete
                .forEach(ac->abilityCourseRepository.save(ac));

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

    private static void checkTitleValidity(AbilityDto abilityDto) {
        if (TitleOrDescriptionValidator.isValidTitle(abilityDto.getName())) {
            throw new IllegalArgumentException("The name of ability doesn't respect rules");
        }
    }

    private static void checkBlank(AbilityDto abilityDto) {
        if (StringUtils.isBlank(abilityDto.getName())) {
            throw new IllegalArgumentException("The name of ability can't be blank");
        }
    }

    private void checkNameExistence(AbilityDto abilityDto) throws DuplicateException {
        if (abilityRepository.existsByNameAndDeletedFalse(abilityDto.getName())) {
            throw new DuplicateException("The name of ability already exists");
        }
    }

}
