package com.fincons.service.courselesson;

import com.fincons.dto.CourseLessonDto;
import com.fincons.entity.CourseLesson;
import com.fincons.exception.DuplicateException;
import java.util.List;

public interface ICourseLessonService {

    List<CourseLesson> getCourseLessonList();

    CourseLesson addCourseLesson(CourseLessonDto courseLessonDto) throws DuplicateException;

    CourseLesson updateCourseLesson(long id, CourseLessonDto courseLessonDto) throws DuplicateException;

    void deleteCourseLesson(long id);

    CourseLesson getCourseLessonById(long id);


}
