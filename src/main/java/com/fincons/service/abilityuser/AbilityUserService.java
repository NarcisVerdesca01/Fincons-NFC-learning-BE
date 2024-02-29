package com.fincons.service.abilityuser;

import com.fincons.dto.AbilityUserDto;
import com.fincons.entity.Ability;
import com.fincons.entity.AbilityUser;
import com.fincons.entity.Course;
import com.fincons.entity.CourseLesson;
import com.fincons.entity.Lesson;
import com.fincons.entity.User;
import com.fincons.exception.CourseException;
import com.fincons.exception.CourseLessonException;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.LessonException;
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
    public AbilityUser addAbilityUser(AbilityUserDto abilityUserDto) {
        return abilityUserRepository.save(abilityUserMapper.mapDtoToAbilityUser(abilityUserDto));
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
}
