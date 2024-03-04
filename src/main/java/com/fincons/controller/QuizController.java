package com.fincons.controller;

import com.fincons.dto.QuizDto;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.QuizMapper;
import com.fincons.service.quiz.IQuizService;
import com.fincons.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class QuizController {

    @Autowired
    private IQuizService iQuizService;
    @Autowired
    private QuizMapper quizMapper;

    @GetMapping("${quiz.get-all-quiz}")
    public ResponseEntity<ApiResponse<List<QuizDto>>> getAllQuiz(){
        List<QuizDto> questionDtoList= iQuizService.findAllQuiz()
                .stream()
                .map(s->quizMapper.mapQuizToQuizDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<QuizDto>>builder()
                .data(questionDtoList)
                .build());
    }
    @GetMapping("${quiz.get-by-id}/{id}")
    public ResponseEntity<ApiResponse<QuizDto>> getById(@PathVariable long id){
        try{
            QuizDto questionDto= quizMapper.mapQuizToQuizDto(iQuizService.findById(id));
            return ResponseEntity.ok().body(ApiResponse.<QuizDto>builder()
                    .data(questionDto)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<QuizDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
    @PostMapping("${quiz.create}")
    public ResponseEntity<ApiResponse<QuizDto>> createQuiz(@RequestBody QuizDto quizDto) {
        try {
            QuizDto questionDtoToShow = quizMapper.mapQuizToQuizDto(iQuizService.createQuiz(quizDto));
            return ResponseEntity.ok().body(ApiResponse.<QuizDto>builder()
                    .data(questionDtoToShow)
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(ApiResponse.<QuizDto>builder()
                    .message(exception.getMessage())
                    .build());
        }
    }

    @PutMapping("${quiz.update}/{id}")
    public ResponseEntity<ApiResponse<String>> updateQuiz(@PathVariable long id,@RequestBody QuizDto quizDto) {
        try {
            iQuizService.updateQuiz(id,quizDto);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The quiz has been successfully updated!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
    @DeleteMapping("${quiz.delete}/{id}")
    public ResponseEntity<ApiResponse<String>> deleteQuiz(@PathVariable long id) {
        try {
            iQuizService.deleteQuiz(id);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The quiz has been successfully deleted!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }








}
