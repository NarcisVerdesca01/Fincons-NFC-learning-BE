package com.fincons.service.course;

import com.fincons.dto.CourseDto;
import com.fincons.entity.Course;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.UserDataException;
import java.util.List;

public interface ICourseService {

    List<Course> findAllCourses();

    Course createCourse(CourseDto courseDto) throws  DuplicateException;

    Course findCourseById(long id);

    void deleteCourse(long id);

    List<Course> findDedicatedCourses() throws UserDataException;

    Course updateCourse(long id, CourseDto courseDto) throws DuplicateException;

    Course findCourseByName(String name);
}
