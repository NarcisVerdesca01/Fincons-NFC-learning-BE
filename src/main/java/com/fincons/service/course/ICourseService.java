package com.fincons.service.course;

import com.fincons.dto.CourseDto;
import com.fincons.entity.Course;
import com.fincons.exception.CourseException;
import com.fincons.exception.UserDataException;

import java.util.Arrays;
import java.util.List;

public interface ICourseService {

    List<Course> findAllCourses();

    Course createCourse(CourseDto courseDto) throws CourseException;

    Course findCourseById(long id);

    void deleteCourse(long id);

    List<Course> findDedicatedCourses(String email) throws UserDataException;


    //Course updateCourse(long id, CourseDto courseDto) throws CourseException;
}
