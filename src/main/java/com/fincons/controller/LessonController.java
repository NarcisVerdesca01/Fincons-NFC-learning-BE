package com.fincons.controller;


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
}
