package com.fincons.mapper;

import com.fincons.dto.CourseLessonDto;
import com.fincons.entity.CourseLesson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseLessonMapper {

    private final ModelMapper modelMapperStandard = new ModelMapper();
    private CourseMapper courseMapper;
    private LessonMapper lessonMapper;


    public List<CourseLessonDto> mapCourseLessonListToCourseLessonDtoList(List<CourseLesson> courseLesson) {
        return courseLesson.stream()
                .map(this::mapCourseLessonEntityToDto)
                .collect(Collectors.toList());
    }

    public CourseLessonDto mapCourseLessonEntityToDto(CourseLesson courseLesson) {
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
