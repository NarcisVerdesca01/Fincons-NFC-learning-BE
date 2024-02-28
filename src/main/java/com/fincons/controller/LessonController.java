package com.fincons.controller;

import com.fincons.dto.CourseDto;
import com.fincons.dto.LessonDto;
import com.fincons.mapper.LessonMapper;
import com.fincons.service.content.IContentService;
import com.fincons.service.lesson.ILessonService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


import com.fincons.mapper.CourseMapper;
import com.fincons.service.course.ICourseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class LessonController {

   @Autowired
   private ILessonService iLessonService;

   @Autowired
   private LessonMapper lessonMapper;

    @GetMapping("${course.get-all-lessons}")
    public ResponseEntity<ApiResponse<List<LessonDto>>> getAllLessons(){
        List<LessonDto> lessonDtoList= iLessonService.findAllLessons()
                .stream()
                .map(s->lessonMapper.mapLessonToLessonDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<LessonDto>>builder()
                .data(lessonDtoList)
                .build());
    }



}
