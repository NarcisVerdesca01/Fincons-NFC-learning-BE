package com.fincons.service.lesson;

import com.fincons.dto.LessonDto;
import com.fincons.entity.Lesson;
import com.fincons.exception.DuplicateException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface ILessonService {
    List<Lesson> findAllLessons();

    List<Lesson> findAllNotAssociatedLessons();

    Lesson findLessonById(long id);

    Lesson createLesson(LessonDto lessonDto) throws DuplicateException;

    Lesson updateLesson(long id, LessonDto lessonDto) throws DuplicateException;

    void deleteLesson(long id);

    Lesson associateContentToLesson(long lessonId, long contentId) throws DuplicateException, SQLIntegrityConstraintViolationException;
}
