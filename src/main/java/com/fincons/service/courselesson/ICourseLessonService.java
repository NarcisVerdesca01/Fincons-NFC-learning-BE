package com.fincons.service.courselesson;

import com.fincons.dto.CourseLessonDto;
import com.fincons.entity.CourseLesson;
import com.fincons.exception.CourseException;
import com.fincons.exception.CourseLessonException;
import com.fincons.exception.LessonException;

import java.util.List;

public interface ICourseLessonService {
    List<CourseLesson> getCourseLessonList();

    CourseLesson addCourseLesson(CourseLessonDto courseLessonDto) throws CourseException, LessonException, CourseLessonException;

    CourseLesson updateCourseLesson(long id, CourseLessonDto courseLessonDto) throws CourseLessonException, CourseException, LessonException;

    void deleteCourseLesson(long id) throws CourseLessonException;
}
