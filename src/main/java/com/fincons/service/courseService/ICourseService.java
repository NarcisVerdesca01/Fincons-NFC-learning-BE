package com.fincons.service.courseService;

import com.fincons.dto.CourseDto;
import com.fincons.entity.Course;
import com.fincons.exception.CourseException;

import java.util.Arrays;
import java.util.List;

public interface ICourseService {

    List<Course> findAllCourses();

    Course createCourse(CourseDto courseDto) throws CourseException;
}
