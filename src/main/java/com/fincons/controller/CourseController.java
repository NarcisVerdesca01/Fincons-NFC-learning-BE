package com.fincons.controller;

import com.fincons.dto.CourseDto;
import com.fincons.exception.CourseException;
import com.fincons.mapper.CourseMapper;
import com.fincons.service.courseService.ICourseService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class CourseController {

    @Autowired
    private ICourseService iCourseService;

    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("${course.get-all-courses}")
    public ResponseEntity<List<CourseDto>> getAllCourses(){
       List<CourseDto> coursesDtoList= iCourseService.findAllCourses()
               .stream()
               .map(c->courseMapper.mapCourseToCourseDto(c))
               .toList();
            return ResponseEntity.ok().body(coursesDtoList);
    }

    @PostMapping("${course.create}")
    public ResponseEntity<?> createCourse(@RequestBody CourseDto courseDto){
        try{
            CourseDto courseDtoToShow = courseMapper.mapCourseToCourseDto(iCourseService.createCourse(courseDto));
            return ResponseEntity.ok().body(courseDtoToShow);
        }catch(CourseException courseException){
            return  ResponseEntity.badRequest().body(courseException.getMessage());
        }
    }
}
