package com.fincons.service.course;

import com.fincons.dto.AbilityDto;
import com.fincons.dto.CourseDto;
import com.fincons.entity.Ability;
import com.fincons.entity.Course;
import com.fincons.entity.User;
import com.fincons.exception.CourseException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityMapper;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.CourseRepository;
import com.fincons.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private AbilityMapper abilityMapper;

    @Override
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }


    @Override
    public Course createCourse(CourseDto courseDto) throws CourseException {

        if(StringUtils.isBlank(courseDto.getName()) || StringUtils.isBlank(courseDto.getDescription()) || courseDto.getAbilities()==null){
            throw new CourseException("Name, description or requirements not present");
        }
        if(courseRepository.existsByName(courseDto.getName())){
            throw new CourseException("The name of course already exist");
        }

        Course course = new Course();
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());

        List<Ability> abilitiesForNewCourse = new ArrayList<>();

        // Associa le abilità all'utente
        course.setAbilities(assignAbilities(courseDto, abilitiesForNewCourse));
        return courseRepository.save(course);
    }

    private List<Ability> assignAbilities(CourseDto courseDto, List<Ability> abilitiesForNewCourse) {
        // Per ogni abilità nel DTO dell'utente
        for (AbilityDto abilityDto : courseDto.getAbilities()) {

            // Controlla se l'abilità esiste già nel repository
            Ability existingAbility = abilityRepository.findByName(abilityDto.getName());

            if (existingAbility != null) {
                // Se l'abilità esiste già, associa quella esistente all'utente
                abilitiesForNewCourse.add(existingAbility);

            } else {
                // Se l'abilità non esiste, crea una nuova abilità nel repository e associa all'utente
                Ability newAbility = new Ability();
                newAbility.setName(abilityDto.getName());
                abilityRepository.save(newAbility);
                abilitiesForNewCourse.add(newAbility);
            }
        }
        return abilitiesForNewCourse;
    }

    @Override
    public Course findCourseById(long id)  {
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
    public List<Course> findDedicatedCourses(String email) {

        if(!userRepository.existsByEmail(email)){
            throw new ResourceNotFoundException("User does not exist");
        }

        // se il ruolo dell'utente è admin restituisci tutti i corsi
        if(userRepository.findByEmail(email)
                .getRoles()
                .stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"))){
            return courseRepository.findAll();
        }

        User userToSeeAbilities = userRepository.findByEmail(email);
        List<Ability> userAbilities = userToSeeAbilities.getAbilities();

        // Cerca i corsi che richiedono almeno una delle abilità dell'utente
        List<Course> list = new ArrayList<>();
        for (Course course : courseRepository.findAll()) {
            if (course.getAbilities().stream()
                    .anyMatch(courseAbility -> userAbilities.contains(courseAbility))) {
                list.add(course);
            }
        }
        return list;




    }


}
