package com.fincons.service.abilitycourse;

import com.fincons.dto.AbilityCourseDto;
import com.fincons.entity.AbilityCourse;

import java.util.List;

public interface IAbilityCourseService {
    List<AbilityCourse> getAllAbilityCourse();

    AbilityCourse addAbilityCourse(AbilityCourseDto abilityCourseDto);
}
