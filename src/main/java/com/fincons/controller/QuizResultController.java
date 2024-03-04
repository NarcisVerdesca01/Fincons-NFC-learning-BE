package com.fincons.controller;


import com.fincons.mapper.QuizResultMapper;
import com.fincons.service.quizresult.IQuizResultService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class QuizResultController {


    private IQuizResultService iQuizResultService;

    private QuizResultMapper quizResultMapper;



}
