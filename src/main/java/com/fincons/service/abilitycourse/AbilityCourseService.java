package com.fincons.service.abilitycourse;

import com.fincons.dto.AbilityCourseDto;
import com.fincons.entity.Ability;
import com.fincons.entity.AbilityCourse;
import com.fincons.entity.Course;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityCourseMapper;
import com.fincons.repository.AbilityCourseRepository;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AbilityCourseService implements IAbilityCourseService{

    @Autowired
    private AbilityCourseRepository abilityCourseRepository;

    @Autowired
    private AbilityCourseMapper abilityCourseMapper;
    @Autowired
    private AbilityRepository abilityRepository;

    @Autowired
    private CourseRepository courseRepository;


    @Override
    public List<AbilityCourse> getAllAbilityCourse() {
        return abilityCourseRepository.findAllByDeletedFalse();
    }

    @Override
    public AbilityCourse addAbilityCourse(AbilityCourseDto abilityCourseDto) throws DuplicateException {

        Ability existingAbility = abilityRepository.findByIdAndDeletedFalse(abilityCourseDto.getAbility().getId());
        Course existingCourse = courseRepository.findByIdAndDeletedFalse(abilityCourseDto.getCourse().getId());
        if(existingCourse == null){
            throw new ResourceNotFoundException("Course does not exist");
        }
        if(existingAbility == null){
            throw new ResourceNotFoundException("Ability does not exist");
        }
        if(abilityCourseRepository.existsByAbilityAndCourseAndDeletedFalse(existingAbility,existingCourse)){
            throw new DuplicateException("The Ability-Course association already exists");
        }
        AbilityCourse abilityCourseToSave = new AbilityCourse(existingCourse, existingAbility);

        return abilityCourseRepository.save(abilityCourseToSave) ;
    }

    @Override
    public AbilityCourse updateAbilityCourse(long id, AbilityCourseDto abilityCourseDto) throws  DuplicateException {

        AbilityCourse existingAbilityCourse = abilityCourseRepository.findByIdAndDeletedFalse(id);
        if(existingAbilityCourse == null){
            throw new ResourceNotFoundException("Ability-Course association does not exist");
        }

        Ability existingAbilityToAssociate = abilityRepository.findByIdAndDeletedFalse(abilityCourseDto.getAbility().getId());
        Course existingCourseToAssociate = courseRepository.findByIdAndDeletedFalse(abilityCourseDto.getCourse().getId());

        existingAbilityCourse.setAbility(existingAbilityToAssociate);
        existingAbilityCourse.setCourse(existingCourseToAssociate);

        return abilityCourseRepository.save(existingAbilityCourse);
    }

    @Override
    public void deleteAbilityCourse(long id) throws ResourceNotFoundException {
        if (!abilityCourseRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The course-ability association does not exist") ;
        }
        abilityCourseRepository.deleteById(id);
    }

    @Override
    public AbilityCourse getAbilityCourseById(long id) {
        if (!abilityCourseRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The ability-course associaiton does not exist!");
        }
        return abilityCourseRepository.findByIdAndDeletedFalse(id);
    }







}
