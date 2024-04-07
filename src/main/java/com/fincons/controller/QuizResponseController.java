package com.fincons.controller;

import com.fincons.dto.QuizResponseDto;
import com.fincons.mapper.QuizResponseMapper;
import com.fincons.service.quizresponse.IQuizResponseService;
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
public class QuizResponseController {

    @Autowired
    private IQuizResponseService iQuizResponseService;

    @Autowired
    private QuizResponseMapper quizResponseMapper;

    @GetMapping("${quiz.get-all-quiz-response}")
    public ResponseEntity<ApiResponse<List<QuizResponseDto>>> getAllQuizResponse(){
        List<QuizResponseDto> quizResponseList= quizResponseMapper.mapQResponseEntityToQResponseDTOSList(iQuizResponseService.findAllQuizResponse());

        return ResponseEntity.ok().body(ApiResponse.<List<QuizResponseDto>>builder()
                .data(quizResponseList)
                .build());
    }



}
