package com.fincons.controller;

import com.fincons.dto.LessonDto;
import com.fincons.mapper.LessonMapper;
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
    public ResponseEntity<ApiResponse<List<LessonDto>>> getAllLessons() {
        List<LessonDto> lessonDtoList = iLessonService.findAllLessons()
                .stream()
                .map(s -> lessonMapper.mapLessonToLessonDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<LessonDto>>builder()
                .data(lessonDtoList)
                .build());
    }


    /*
    @GetMapping("${lesson.getById}/{id}")
    public ResponseEntity<ApiResponse<LessonDto>> getLessonById(@PathVariable long id){
        try{
            LessonDto lessonDtoToShow = lessonMapper.mapLessonToLessonDto(iLessonService.findLessonById(id));
            return ResponseEntity.ok().body(ApiResponse.<LessonDto>builder()
                    .data(lessonDtoToShow)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<LessonDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
*/


}
