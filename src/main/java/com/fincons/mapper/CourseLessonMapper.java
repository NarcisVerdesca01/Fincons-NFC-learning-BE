package com.fincons.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fincons.dto.CourseLessonDto;
import com.fincons.entity.CourseLesson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;


@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseLessonMapper {

    private final ModelMapper modelMapperStandard = new ModelMapper();;
    private CourseMapper courseMapper;
    private LessonMapper lessonMapper;

    public CourseLessonDto mapCourseLessonToCourseLessonDto(CourseLesson courseLesson){
        return modelMapperStandard.map(courseLesson, CourseLessonDto.class);
    }
    public CourseLesson mapToEntity(CourseLessonDto courseLessonDto){
        return modelMapperStandard.map(courseLessonDto, CourseLesson.class);
    }

    public List<CourseLessonDto> mapCourseLessonListToAbilityCourseDtoList(List<CourseLesson> courseLesson) {
        return courseLesson.stream()
                .map(this::mapCourseLessonEntityToDto)
                .collect(Collectors.toList());
    }

    private CourseLessonDto mapCourseLessonEntityToDto(CourseLesson courseLesson) {
        CourseLessonDto courseLessonDto = new CourseLessonDto();
        courseLessonDto.setId(courseLesson.getId());
        courseLessonDto.setCourse(courseMapper.mapCourseToCourseDto(courseLesson.getCourse()));
        courseLessonDto.setLesson(lessonMapper.mapLessonToLessonDto(courseLesson.getLesson()));
        return courseLessonDto;
    }
    public CourseLesson mapCourseLessonDtoToEntity(CourseLessonDto courseLessonDto) {
        CourseLesson courseLesson = new CourseLesson();
        courseLesson.setId(courseLessonDto.getId());
        courseLesson.setCourse(courseMapper.mapDtoToCourse(courseLessonDto.getCourse()));
        courseLesson.setLesson(lessonMapper.mapDtoToLessonEntity(courseLessonDto.getLesson()) );
        return courseLesson;
    }

}
