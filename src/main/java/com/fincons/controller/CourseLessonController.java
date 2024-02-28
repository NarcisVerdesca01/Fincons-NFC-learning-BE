package com.fincons.controller;

import com.fincons.dto.AbilityCourseDto;
import com.fincons.dto.CourseLessonDto;
import com.fincons.exception.CourseException;
import com.fincons.exception.CourseLessonException;
import com.fincons.exception.LessonException;
import com.fincons.mapper.CourseLessonMapper;
import com.fincons.service.courselesson.ICourseLessonService;
import com.fincons.service.lesson.ILessonService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class CourseLessonController {

    private ICourseLessonService iCourseLessonService;

    private CourseLessonMapper courseLessonMapper;

    @GetMapping("${course-lesson.list}")
    public ResponseEntity<ApiResponse<List<CourseLessonDto>>> getAllCourseLesson(){

        List<CourseLessonDto> courseLessonDtos = courseLessonMapper
                .mapCourseLessonListToAbilityCourseDtoList(iCourseLessonService.getCourseLessonList()) ;
        return ResponseEntity.ok().body(ApiResponse.<List<CourseLessonDto>>builder()
                .data(courseLessonDtos)
                .build());
    }

    @PostMapping("${course-lesson.add}")
    public ResponseEntity<ApiResponse<CourseLessonDto>> addAbilityCourse(@RequestBody CourseLessonDto courseLessonDto) {
        try{
            CourseLessonDto courseLessonDtoToShow = courseLessonMapper
                    .mapCourseLessonToCourseLessonDto(iCourseLessonService.addCourseLesson(courseLessonDto));
            return ResponseEntity.ok().body(ApiResponse.<CourseLessonDto>builder()
                    .data(courseLessonDtoToShow)
                    .build());
        }catch(CourseException | LessonException | CourseLessonException  exception){
            return ResponseEntity.badRequest().body(ApiResponse.<CourseLessonDto>builder()
                    .message(exception.getMessage())
                    .build());
        }
    }

    @PutMapping("${course-lesson.update}/{id}")
    public ResponseEntity<ApiResponse<CourseLessonDto>> updateCourseLesson(@PathVariable int id, @RequestBody CourseLessonDto courseLessonDto){



            CourseLessonDto courseLessonDtoToShow = courseLessonMapper.mapCourseLessonToCourseLessonDto(iCourseLessonService.updateCourseLesson(id,courseLessonDto));

            return ResponseEntity.ok().body(ApiResponse.<CourseLessonDto>builder()
                    .data(courseLessonDtoToShow)
                    .build());

    }





}
