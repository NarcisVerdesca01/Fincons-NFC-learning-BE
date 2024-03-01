package com.fincons.service.abilitycourse;

import com.fincons.dto.AbilityCourseDto;
import com.fincons.dto.AbilityUserDto;
import com.fincons.entity.Ability;
import com.fincons.entity.AbilityCourse;
import com.fincons.entity.AbilityUser;
import com.fincons.entity.Course;
import com.fincons.entity.CourseLesson;
import com.fincons.entity.Lesson;
import com.fincons.exception.AbilityCourseException;
import com.fincons.exception.AbilityException;
import com.fincons.exception.CourseException;
import com.fincons.exception.CourseLessonException;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.LessonException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityCourseMapper;
import com.fincons.mapper.AbilityUserMapper;
import com.fincons.repository.AbilityCourseRepository;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.AbilityUserRepository;
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
        return abilityCourseRepository.findAll();
    }

    @Override
    public AbilityCourse addAbilityCourse(AbilityCourseDto abilityCourseDto) throws DuplicateException {

        Ability existingAbility = abilityRepository.findById(abilityCourseDto.getAbility().getId()).orElseThrow(()-> new ResourceNotFoundException("Ability does not exist"));
        Course existingCourse = courseRepository.findById(abilityCourseDto.getCourse().getId()).orElseThrow(()-> new ResourceNotFoundException("Course does not exist"));
        if(abilityCourseRepository.existsByAbilityAndCourse(existingAbility,existingCourse)){
            throw new DuplicateException("The Ability-Course association already exists");
        }
        AbilityCourse abilityCourseToSave = new AbilityCourse(existingCourse, existingAbility);

        return abilityCourseRepository.save(abilityCourseToSave) ;
    }

    @Override
    public AbilityCourse updateAbilityCourse(long id, AbilityCourseDto abilityCourseDto) throws  DuplicateException {

        AbilityCourse existingAbilityCourse = abilityCourseRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Ability-Course association does not exist"));

        Ability existingAbilityToAssociate = abilityRepository.findById(abilityCourseDto.getAbility().getId()).orElseThrow(()-> new ResourceNotFoundException("Ability does not exist"));
        Course existingCourseToAssociate = courseRepository.findById(abilityCourseDto.getCourse().getId()).orElseThrow(()-> new ResourceNotFoundException("Course does not exist"));

        if(abilityCourseRepository.existsByAbilityAndCourse(existingAbilityToAssociate,existingCourseToAssociate)){
            throw new DuplicateException("Ability-CourseAssociation already exists");
        }
        existingAbilityCourse.setAbility(existingAbilityToAssociate);
        existingAbilityCourse.setCourse(existingCourseToAssociate);

        return abilityCourseRepository.save(existingAbilityCourse);
    }

    @Override
    public void deleteAbilityCourse(long id) throws AbilityCourseException {
        if (!abilityCourseRepository.existsById(id)) {
            throw new AbilityCourseException("The course-ability association does not exist") ;
        }
        abilityCourseRepository.deleteById(id);
    }

    @Override
    public AbilityCourse getAbilityCourseById(long id) {
        return abilityCourseRepository
                .findById(id).orElseThrow(()-> new ResourceNotFoundException("The Ability-Course association does not exist"));
    }

}
