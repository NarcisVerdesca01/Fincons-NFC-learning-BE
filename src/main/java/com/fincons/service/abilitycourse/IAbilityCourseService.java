package com.fincons.service.abilitycourse;

import com.fincons.dto.AbilityCourseDto;
import com.fincons.entity.AbilityCourse;
import com.fincons.exception.DuplicateException;
import java.util.List;

public interface IAbilityCourseService {

    List<AbilityCourse> getAllAbilityCourse();

    AbilityCourse addAbilityCourse(AbilityCourseDto abilityCourseDto) throws DuplicateException;

    AbilityCourse updateAbilityCourse(long id, AbilityCourseDto abilityCourseDto) throws  DuplicateException;

    void deleteAbilityCourse(long id);

    AbilityCourse getAbilityCourseById(long id);


}
