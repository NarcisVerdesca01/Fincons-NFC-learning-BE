package com.fincons.controller;

import com.fincons.dto.CourseDto;
import com.fincons.dto.LessonDto;
import com.fincons.entity.Ability;
import com.fincons.entity.Course;
import com.fincons.entity.Lesson;
import com.fincons.exception.CourseException;
import com.fincons.mapper.LessonMapper;
import com.fincons.service.content.IContentService;
import com.fincons.service.lesson.ILessonService;
import com.fincons.utility.ApiResponse;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class LessonController {

   @Autowired
   private ILessonService iLessonService;

   @Autowired
   private LessonMapper lessonMapper;

    @GetMapping("${lesson.get-all-lessons}")
    public ResponseEntity<ApiResponse<List<LessonDto>>> getAllLessons(){
        List<LessonDto> lessonDtoList= iLessonService.findAllCourses()
                .stream()
                .map(s->lessonMapper.mapLessonToLessonDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<LessonDto>>builder()
                .data(lessonDtoList)
                .build());
    }

    @PostMapping("${lesson.create}")
    public ResponseEntity<ApiResponse<LessonDto>> createLesson(@RequestBody LessonDto lessonDto) {
        try {
            LessonDto lessonDtoToShow = lessonMapper.mapLessonToLessonDto(iLessonService.createLesson(lessonDto));
            return ResponseEntity.ok().body(ApiResponse.<LessonDto>builder()
                    .data(lessonDtoToShow)
                    .build());
        } catch (CourseException courseException) {
            return ResponseEntity.badRequest().body(ApiResponse.<LessonDto>builder()
                    .message(courseException.getMessage())
                    .build());
        }
    }

}
