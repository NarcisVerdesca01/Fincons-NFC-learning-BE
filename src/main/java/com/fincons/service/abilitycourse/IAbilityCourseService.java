package com.fincons.service.abilitycourse;

import com.fincons.dto.AbilityCourseDto;
import com.fincons.entity.AbilityCourse;
import com.fincons.exception.AbilityCourseException;
import com.fincons.exception.AbilityException;
import com.fincons.exception.CourseException;
import com.fincons.exception.CourseLessonException;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.LessonException;

import java.util.List;

public interface IAbilityCourseService {
    List<AbilityCourse> getAllAbilityCourse();

    AbilityCourse addAbilityCourse(AbilityCourseDto abilityCourseDto) throws DuplicateException;

    AbilityCourse updateAbilityCourse(long id, AbilityCourseDto abilityCourseDto) throws  DuplicateException;

    void deleteAbilityCourse(long id) throws AbilityCourseException;

    AbilityCourse getAbilityCourseById(long id);
}
