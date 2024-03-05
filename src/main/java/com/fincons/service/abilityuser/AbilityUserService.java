package com.fincons.service.abilityuser;

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
        return abilityUserRepository.findAll();
    }

    @Override
    public AbilityUser addAbilityUser(AbilityUserDto abilityUserDto) throws DuplicateException {

        Ability existingAbility = abilityRepository.findById(abilityUserDto.getAbility().getId()).orElseThrow(()-> new ResourceNotFoundException("Ability does not exist"));
        User existingUser = userRepository.findById(abilityUserDto.getUser().getId()).orElseThrow(()-> new ResourceNotFoundException("Ability does not exist"));
        if(abilityUserRepository.existsByAbilityAndUser(existingAbility,existingUser)){
            throw new DuplicateException("The Ability-User association already exists");
        }
        AbilityUser abilityUserToSave = new AbilityUser(existingUser, existingAbility);

        return abilityUserRepository.save(abilityUserToSave);
    }

    @Override
    public AbilityUser getAbilityUserById(long id) {
        return abilityUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The ability-user association does not exist!"));
    }

    @Override
    public AbilityUser updateAbilityUser(long id, AbilityUserDto abilityUserDto) throws DuplicateException {

        AbilityUser existingAbilityUser = abilityUserRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ability-User association does not exist"));

        Ability existingAbilityToAssociate = abilityRepository.findById(abilityUserDto.getAbility().getId()).orElseThrow(()-> new ResourceNotFoundException("Course does not exist"));
        User existingUserToAddAssociate = userRepository.findById(abilityUserDto.getUser().getId()).orElseThrow(()-> new ResourceNotFoundException("Lesson does not exist"));

        if(abilityUserRepository.existsByAbilityAndUser(existingAbilityToAssociate,existingUserToAddAssociate)){
            throw new DuplicateException("Association between ability and user already exists");
        }

        existingAbilityUser.setAbility(existingAbilityToAssociate);
        existingAbilityUser.setUser(existingUserToAddAssociate);

        return abilityUserRepository.save(existingAbilityUser);

    }

    @Override
    public void deleteAbilityUser(long id) {
        if (!abilityUserRepository.existsById(id)) {
            throw new ResourceNotFoundException("The course-lesson association does not exist") ;
        }
        abilityUserRepository.deleteById(id);
    }
}
