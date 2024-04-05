package com.fincons.service.course;

import com.fincons.controller.AuthController;
import com.fincons.dto.CourseDto;
import com.fincons.entity.Ability;
import com.fincons.entity.AbilityCourse;
import com.fincons.entity.AbilityUser;
import com.fincons.entity.Course;
import com.fincons.entity.CourseLesson;
import com.fincons.entity.User;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityMapper;
import com.fincons.repository.AbilityCourseRepository;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.AbilityUserRepository;
import com.fincons.repository.CourseLessonRepository;
import com.fincons.repository.CourseRepository;
import com.fincons.repository.UserRepository;
import com.fincons.utility.TitleOrDescriptionValidator;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CourseService implements ICourseService {



    private static final Logger LOG = LoggerFactory.getLogger(CourseService.class);

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
    private CourseLessonRepository courseLessonRepository;

    @Autowired
    private AbilityMapper abilityMapper;

    @Override
    public List<Course> findAllCourses() {
        return courseRepository.findAllByDeletedFalse();
    }

    @Override
    public List<Course> findAllCoursesWithoutLesson() {
        return courseRepository.findAllByDeletedFalseAndCourseLessonsIsNull();
    }

    @Override
    public Course createCourse(CourseDto courseDto) throws  DuplicateException {

        checkBlank(courseDto);
        checkNameExistence(courseDto);
        checkNameValidity(courseDto);
        checkDescriptionValidity(courseDto);

        Course course = new Course();
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setBackgroundImage(courseDto.getBackgroundImage());

        if(courseDto.getImageResource() != null){
            course.setImageResource(courseDto.getImageResource());
        }

        return courseRepository.save(course);
    }

    private static void checkDescriptionValidity(CourseDto courseDto) {
        if (!TitleOrDescriptionValidator.isValidDescription(courseDto.getDescription())) {
            throw new IllegalArgumentException("The description doesn't respect rules");
        }
    }

    private static void checkNameValidity(CourseDto courseDto) {
        if (!TitleOrDescriptionValidator.isValidTitle(courseDto.getName())) {
            throw new IllegalArgumentException("The name of course doesn't respect rules");
        }
    }

    private void checkNameExistence(CourseDto courseDto) throws DuplicateException {
        if (courseRepository.existsByNameAndDeletedFalse(courseDto.getName())) {
            throw new DuplicateException("The name of course already exist");
        }
    }

    private static void checkBlank(CourseDto courseDto) {
        if (StringUtils.isBlank(courseDto.getName()) || StringUtils.isBlank(courseDto.getDescription()) || StringUtils.isBlank(courseDto.getBackgroundImage())) {
            throw new IllegalArgumentException("Name, description or background image not present");
        }
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

        List<CourseLesson> courseLessonAssociationsToDelete = courseLessonRepository.findAllByDeletedFalse()
                .stream()
                .filter(cl-> cl.getCourse().getId()==id)
                .toList();

        courseLessonAssociationsToDelete
                .forEach(cl->cl.setDeleted(true));
        courseLessonAssociationsToDelete
                .forEach(cl->courseLessonRepository.save(cl));

        List<AbilityCourse> abilityCoursesListAssociationToDelete = abilityCourseRepository.findAllByDeletedFalse()
                .stream()
                .filter(cl-> cl.getCourse().getId()==id)
                .toList();

        abilityCoursesListAssociationToDelete
                .forEach(cl->cl.setDeleted(true));
        abilityCoursesListAssociationToDelete
                .forEach(cl->abilityCourseRepository.save(cl));


        Course courseToDelete = courseRepository.findByIdAndDeletedFalse(id);
        courseToDelete.setDeleted(true);
        courseRepository.save(courseToDelete);
        LOG.info("{} successfully deleted course  ' {} ' , When: {} ", SecurityContextHolder.getContext().getAuthentication().getName(), courseToDelete.getName(), LocalDateTime.now());
    }

    private void validateCourseById(long id) {
        if (!courseRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The course does not exist");
        }
    }
    @Override

    public List<Course> findDedicatedCourses() {

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();

        if (loggedUser.isEmpty()) {

            throw new ResourceNotFoundException("User with this email doesn't exist");

        }

        if (!userRepository.existsByEmail(loggedUser)) {

            throw new ResourceNotFoundException("User does not exist");

        }

        User user = userRepository.findByEmail(loggedUser);

        boolean isUserAdmin = user.getRoles()

                .stream()

                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        List<AbilityCourse> abilityCourses = abilityCourseRepository.findAllByDeletedFalse();

        List<AbilityUser> abilityUsers = abilityUserRepository.findAllByDeletedFalse();

        List<AbilityUser> abilitiesOfInterestedUser = abilityUsers

                .stream()

                .filter(abilityUser -> abilityUser.getUser().getEmail().equals(loggedUser))

                .toList();

        List<String> abilityNameOfInterestedUser = abilitiesOfInterestedUser

                .stream()

                .map(a -> a.getAbility().getName())

                .toList();

        Set<Course> uniqueCourses = new HashSet<>();

        for (AbilityCourse abilityCourse : abilityCourses) {

            if (abilityNameOfInterestedUser.contains(abilityCourse.getAbility().getName())) {

                uniqueCourses.add(abilityCourse.getCourse());

            }

        }

        return new ArrayList<>(uniqueCourses);

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

        LOG.info("{} update course  ' {} ' , When: {} ", SecurityContextHolder.getContext().getAuthentication().getName(), courseToModify.getName(), LocalDateTime.now());
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






