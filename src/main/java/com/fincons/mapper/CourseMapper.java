package com.fincons.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fincons.dto.CourseDto;
import com.fincons.entity.Course;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public CourseDto mapCourseToCourseDto(Course course){
        return modelMapper.map(course, CourseDto.class);
    }

    public Course mapDtoToCourse(CourseDto courseDto){
        return modelMapper.map(courseDto, Course.class);
    }


}
