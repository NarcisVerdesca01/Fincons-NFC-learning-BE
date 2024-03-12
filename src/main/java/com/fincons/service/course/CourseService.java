package com.fincons.service.course;

import com.fincons.dto.CourseDto;
import com.fincons.entity.Ability;
import com.fincons.entity.AbilityCourse;
import com.fincons.entity.AbilityUser;
import com.fincons.entity.Course;
import com.fincons.entity.User;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityMapper;
import com.fincons.repository.AbilityCourseRepository;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.AbilityUserRepository;
import com.fincons.repository.CourseRepository;
import com.fincons.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

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
        return courseRepository.findAllByDeletedFalse();
    }

    @Override
    public Course createCourse(CourseDto courseDto) throws  DuplicateException {

        if (StringUtils.isBlank(courseDto.getName()) || StringUtils.isBlank(courseDto.getDescription()) || StringUtils.isBlank(courseDto.getBackgroundImage())) {
            throw new IllegalArgumentException("Name, description or background image not present");
        }
        if (courseRepository.existsByNameAndDeletedFalse(courseDto.getName())) {
            throw new DuplicateException("The name of course already exist");
        }

        Course course = new Course();
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setBackgroundImage(courseDto.getBackgroundImage());

        if(courseDto.getImageResource() != null){
            course.setImageResource(courseDto.getImageResource());
        }

        return courseRepository.save(course);
    }


    @Override
    public Course findCourseById(long id) {

        if(!courseRepository.existsByIdAndDeletedFalse(id)){
            throw new ResourceNotFoundException("The course does not exist!");
        }

        return  courseRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public void deleteCourse(long id) {
        validateCourseById(id);
        Course courseToDelete = courseRepository.findByIdAndDeletedFalse(id);
        courseToDelete.setDeleted(true);
        courseRepository.save(courseToDelete);
    }

    private void validateCourseById(long id) {
        if (!courseRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The course does not exist");
        }
    }

    @Override
    public List<Course> findDedicatedCourses(){

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(loggedUser);
        if (!userRepository.existsByEmail(loggedUser)) {
            throw new ResourceNotFoundException("User does not exist");
        }
        User user = userRepository.findByEmail(loggedUser);
        boolean isUserAdmin = user.getRoles()
                .stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
        if(isUserAdmin){
            return courseRepository.findAll();
        }
        List<AbilityCourse> abilityCourses = abilityCourseRepository.findAllByDeletedFalse();

        List<AbilityUser> abilityUsers = abilityUserRepository.findAllByDeletedFalse();

        List<AbilityUser> abilitiesOfInterestedUser = abilityUsers
                .stream()
                .filter( abilityUser -> abilityUser.getUser().getEmail().equals(loggedUser))
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
    public Course updateCourse(long id, CourseDto courseDto) throws DuplicateException {

        Course courseToModify = courseRepository.findByIdAndDeletedFalse(id);

        if(courseToModify == null){
            throw new ResourceNotFoundException("Course does not exist");
        }

        if (courseDto.getName() != null) {
            if (!courseRepository.existsByNameAndIdNot(courseDto.getName(), courseToModify.getId())) {
                courseToModify.setName(courseDto.getName());
            } else {
                throw new DuplicateException("Course already exists");
            }
        } else {
            throw new IllegalArgumentException("Course name cannot be null");
        }

        if (courseDto.getBackgroundImage() != null) {
            courseToModify.setBackgroundImage(courseDto.getBackgroundImage());
        }

        if (courseDto.getDescription() != null) {
            courseToModify.setDescription(courseDto.getDescription());
        }

        return courseRepository.save(courseToModify);
    }

    @Override
    public Course findCourseByName(String name) {

        Course course = courseRepository.findByNameAndDeletedFalse(name);
        if(course ==null || name.isEmpty()){
            throw new ResourceNotFoundException("Course " + name + " does not exists!");
        }
        return course;
    }



}






