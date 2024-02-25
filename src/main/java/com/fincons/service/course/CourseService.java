package com.fincons.service.courseService;

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

        Course course = new Course();
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());

        // Per ogni abilità nel DTO del corso
        for(AbilityDto abilityDto : courseDto.getAbilities()){

            // Controlla se l'abilità esiste già nel repository
            Ability existingAbility = abilityRepository.findByName(abilityDto.getNameOfAbility());

            if(existingAbility != null){
                // Se l'abilità esiste già, associa quella esistente al corso
                course.getAbilities().add(existingAbility);
            }else{
                // Se l'abilità non esiste, crea una nuova abilità nel repository e associa al corso
                Ability newAbility = new Ability();
                newAbility.setNameOfAbility(abilityDto.getNameOfAbility());
                abilityRepository.save(newAbility);
                course.getAbilities().add(newAbility);

            }
        }
        return courseRepository.save(course);
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
