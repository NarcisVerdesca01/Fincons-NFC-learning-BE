package com.fincons.service.course;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fincons.dto.AbilityDto;
import com.fincons.dto.CourseDto;
import com.fincons.dto.LessonDto;
import com.fincons.entity.Ability;
import com.fincons.entity.AbilityCourse;
import com.fincons.entity.AbilityUser;
import com.fincons.entity.Course;
import com.fincons.entity.User;
import com.fincons.exception.CourseException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.exception.UserDataException;
import com.fincons.mapper.AbilityMapper;
import com.fincons.repository.AbilityCourseRepository;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.AbilityUserRepository;
import com.fincons.repository.CourseRepository;
import com.fincons.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService implements ICourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AbilityRepository abilityRepository;

    @Autowired
    private AbilityUserRepository abilityUserRepository;

    @Autowired
    private AbilityCourseRepository abilityCourseRepository;

    @Autowired
    private AbilityMapper abilityMapper;

    @Override
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }


    @Override
    public Course createCourse(CourseDto courseDto) throws CourseException {

        if (StringUtils.isBlank(courseDto.getName()) || StringUtils.isBlank(courseDto.getDescription()) || courseDto.getBackgroundImage().length==0) {
            throw new CourseException("Name, description or requirements not present");
        }
        if (courseRepository.existsByName(courseDto.getName())) {
            throw new CourseException(CourseException.courseAlreadyExist());
        }

        Course course = new Course();
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setBackgroundImage(courseDto.getBackgroundImage());

        return courseRepository.save(course);
    }


    @Override
    public Course findCourseById(long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("The course does not exist!");
        }
        return courseRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCourse(long id) {

        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("The course does not exist");
        }

        // Elimina il corso
        courseRepository.deleteById(id);
    }

    @Override
    public List<Course> findDedicatedCourses(String email) throws UserDataException {
        if (!userRepository.existsByEmail(email)) {
            throw new UserDataException("User does not exist");
        }
        User user = userRepository.findByEmail(email);
        boolean isUserAdmin = user.getRoles()
                .stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
        if(isUserAdmin){
            return courseRepository.findAll();
        }
        List<AbilityCourse> abilityCourses = abilityCourseRepository.findAll();

        List<AbilityUser> abilityUsers = abilityUserRepository.findAll();

        List<AbilityUser> abilitiesOfInterestedUser = abilityUsers
                .stream()
                .filter( abilityUser -> abilityUser.getUser().getEmail().equals(email))
                .toList();

        List<String> abilityNameOfInterestedUser = abilitiesOfInterestedUser
                .stream()
                .map(a -> a.getAbility().getName())
                .toList();

        List<AbilityCourse> dedicatedAbilityCourses = abilityCourses
                .stream()
                .filter(abilityCourse -> abilityNameOfInterestedUser.contains(abilityCourse.getAbility().getName()))
                .toList();

        return dedicatedAbilityCourses
                .stream()
                .map(AbilityCourse::getCourse)
                .toList();
    }

    @Override
    public Course updateCourse(long id, CourseDto courseDto) throws CourseException {

        Course courseToModify = courseRepository.findById(id)
                .orElseThrow(() -> new CourseException(CourseException.courseDosNotExist()));

        if (courseDto.getName() != null) {
            courseToModify.setName(courseDto.getName());
        }

        if (courseDto.getBackgroundImage() != null) {
            courseToModify.setBackgroundImage(courseDto.getBackgroundImage());
        }

        if (courseDto.getDescription() != null) {
            courseToModify.setDescription(courseDto.getDescription());
        }

        return courseRepository.save(courseToModify);
    }


}






