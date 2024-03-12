package com.fincons.service.abilityuser;

import com.fincons.dto.AbilityDto;
import com.fincons.dto.AbilityUserDto;
import com.fincons.entity.Ability;
import com.fincons.entity.AbilityUser;
import com.fincons.entity.User;;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityUserMapper;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.AbilityUserRepository;
import com.fincons.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AbilityUserService implements IAbilityUserService{

    @Autowired
    private AbilityUserRepository abilityUserRepository;

    @Autowired
    private AbilityUserMapper abilityUserMapper;

    @Autowired
    private AbilityRepository abilityRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<AbilityUser> getAllAbilityUser() {
        return abilityUserRepository.findAllByDeletedFalse();
    }

    @Override
    public AbilityUser addAbilityUser(long idOfUser, long abilityIdToAssociate) throws DuplicateException {

        Ability existingAbility = abilityRepository.findByIdAndDeletedFalse(abilityIdToAssociate);
        User existingUser = userRepository.findByIdAndDeletedFalse(idOfUser);

        if(existingAbility == null){
            throw new ResourceNotFoundException("Ability does not exist");
        }
        if(existingUser == null){
            throw new ResourceNotFoundException("User does not exist");
        }

        if(abilityUserRepository.existsByUserAndAbilityAndDeletedFalse(existingAbility,existingUser)){
            throw new DuplicateException("The Ability-User association already exists");
        }
        AbilityUser abilityUserToSave = new AbilityUser(existingUser, existingAbility);

        return abilityUserRepository.save(abilityUserToSave);
    }

    @Override
    public AbilityUser getAbilityUserById(long id) {
        if (!abilityUserRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The ability-user associaiton does not exist!");
        }
        return abilityUserRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public AbilityUser updateAbilityUser(long id, AbilityUserDto abilityUserDto) throws DuplicateException {

        AbilityUser existingAbilityUser = abilityUserRepository.findByIdAndDeletedFalse(id);
        if(existingAbilityUser == null){
            throw new ResourceNotFoundException("Ability-User association does not exist");
        }

        Ability existingAbilityToAssociate = abilityRepository.findByIdAndDeletedFalse(abilityUserDto.getAbility().getId());
        User existingUserToAddAssociate = userRepository.findByIdAndDeletedFalse(abilityUserDto.getUser().getId());

        existingAbilityUser.setAbility(existingAbilityToAssociate);
        existingAbilityUser.setUser(existingUserToAddAssociate);

        return abilityUserRepository.save(existingAbilityUser);

    }

    @Override
    public void deleteAbilityUser(long id) {
        if (!abilityUserRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The course-lesson association does not exist") ;
        }
        abilityUserRepository.deleteById(id);
    }


}
