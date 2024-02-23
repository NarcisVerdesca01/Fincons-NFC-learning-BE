package com.fincons.service.courseService;

import com.fincons.dto.CourseDto;
import com.fincons.entity.Course;
import com.fincons.exception.CourseException;
import com.fincons.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService implements ICourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course createCourse(CourseDto courseDto) throws CourseException {

        if(courseDto.getName() == null  || courseDto.getDescription() == null || courseDto.getRequirementsAccess() == null){
            throw new CourseException("Name, description or requirements not present");
        }

        Course course = new Course();
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        course.setRequirementsAccess(courseDto.getRequirementsAccess());

        return courseRepository.save(course);
    }
}
