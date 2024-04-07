package com.fincons.mapper;

import com.fincons.dto.LessonDto;
import com.fincons.entity.Lesson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    private static final ModelMapper modelMapper = new ModelMapper();
    public LessonDto mapLessonToLessonDto(Lesson lesson){
        return modelMapper.map(lesson, LessonDto.class);
    }
    public Lesson mapDtoToLessonEntity(LessonDto lessonDto){
        return modelMapper.map(lessonDto, Lesson.class);
    }

}
