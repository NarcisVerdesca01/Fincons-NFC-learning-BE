package com.fincons.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fincons.dto.CourseDto;
import com.fincons.dto.LessonDto;
import com.fincons.entity.Course;
import com.fincons.entity.Lesson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonMapper {

    private static final ModelMapper modelMapper = new ModelMapper();
    public LessonDto mapLessonToLessonDto(Lesson lesson){
        return modelMapper.map(lesson, LessonDto.class);
    }
    public Lesson mapDtoToLessonEntity(LessonDto lessonDto){
        return modelMapper.map(lessonDto, Lesson.class);
    }
}
