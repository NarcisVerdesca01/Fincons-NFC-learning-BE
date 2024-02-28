package com.fincons.service.lesson;

import com.fincons.dto.LessonDto;
import com.fincons.entity.Lesson;
import com.fincons.exception.LessonException;

import java.util.List;

public interface ILessonService {
    List<Lesson> findAllLessons();

    Lesson findLessonById(long id);

    Lesson createLesson(LessonDto lessonDto) throws LessonException;

    Lesson updateLesson(long id, LessonDto lessonDto) throws LessonException;
}
