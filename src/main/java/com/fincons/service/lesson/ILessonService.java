package com.fincons.service.lesson;

import com.fincons.dto.CourseDto;
import com.fincons.dto.LessonDto;
import com.fincons.entity.Course;
import com.fincons.entity.Lesson;
import com.fincons.exception.CourseException;

import java.util.List;

public interface ILessonService {
    List<Lesson> findAllCourses();
    Lesson createLesson(LessonDto lessonDto) throws CourseException;
}
