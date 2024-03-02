package com.fincons.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fincons.dto.AbilityCourseDto;
import com.fincons.entity.AbilityCourse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbilityCourseMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    private AbilityMapper abilityMapper;
    private CourseMapper courseMapper;

    public AbilityCourseDto mapAbilityCourseToAbilityCourseDto(AbilityCourse abilityCourse){
        return modelMapper.map(abilityCourse, AbilityCourseDto.class);
    }

    public AbilityCourse mapDtoToAbilityCourse(AbilityCourseDto abilityCourseDto){
        return modelMapper.map(abilityCourseDto, AbilityCourse.class);
    }

    public List<AbilityCourseDto> mapAbilityCourseListToAbilityCourseDtoList(List<AbilityCourse> abilityCourseList) {
        return abilityCourseList.stream()
                .map(this::mapAbilityCourseToAbilityCourseDto)
                .collect(Collectors.toList());
    }

    public List<AbilityCourse> mapAbilityCourseDtoListToAbilityCourseList(List<AbilityCourseDto> abilityCourseDtoList) {
        return abilityCourseDtoList.stream()
                .map(this::mapDtoToAbilityCourse)
                .collect(Collectors.toList());
    }
}
