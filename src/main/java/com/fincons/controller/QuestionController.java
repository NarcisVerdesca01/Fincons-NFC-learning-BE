package com.fincons.controller;


import com.fincons.dto.QuestionDto;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.QuestionMapper;
import com.fincons.service.question.IQuestionService;
import com.fincons.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin("*")
@RequestMapping("${application.context}")
public class QuestionController {
    @Autowired
    private IQuestionService iQuestionService;

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("${question.get-all-question}")
    public ResponseEntity<ApiResponse<List<QuestionDto>>> getAllQuestion(){
        List<QuestionDto> questionDtoList= iQuestionService.findAllQuestion()
                .stream()
                .map(s->questionMapper.mapQuestionToQuestionDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<QuestionDto>>builder()
                .data(questionDtoList)
                .build());
    }

    @GetMapping("${question.get-all-question-noassociationquiz}")
    public ResponseEntity<ApiResponse<List<QuestionDto>>> getAllQuestionWithoutAssociatedQuiz(){
        List<QuestionDto> questionDtoList= iQuestionService.findAllQuestionWithoutQuiz()
                .stream()
                .map(s->questionMapper.mapQuestionToQuestionDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<QuestionDto>>builder()
                .data(questionDtoList)
                .build());
    }

    @GetMapping("${question.get-all-question-noassociationanswer}")
    public ResponseEntity<ApiResponse<List<QuestionDto>>> getAllQuestionWithoutAssociatedAnswers(){
        List<QuestionDto> questionDtoList= iQuestionService.findAllQuestionWithoutAnswers()
                .stream()
                .map(s->questionMapper.mapQuestionToQuestionDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<QuestionDto>>builder()
                .data(questionDtoList)
                .build());
    }
    @GetMapping("${question.get-by-id}")
    public ResponseEntity<ApiResponse<QuestionDto>> getById(@RequestParam(name="idQuestion") long id){
        try{
            QuestionDto questionDto= questionMapper.mapQuestionToQuestionDto(iQuestionService.findById(id));
            return ResponseEntity.ok().body(ApiResponse.<QuestionDto>builder()
                    .data(questionDto)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<QuestionDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
    @PostMapping("${question.create}")
    public ResponseEntity<ApiResponse<QuestionDto>> createQuestion(@RequestBody QuestionDto questionDto) {
        try {
            QuestionDto questionDtoToShow = questionMapper.mapQuestionToQuestionDto(iQuestionService.createQuestion(questionDto));
            return ResponseEntity.ok().body(ApiResponse.<QuestionDto>builder()
                    .data(questionDtoToShow)
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(ApiResponse.<QuestionDto>builder()
                    .message(exception.getMessage())
                    .build());
        }
    }

    @PutMapping("${question.update}")
    public ResponseEntity<ApiResponse<String>> updateQuestion(@RequestParam(name="idQuestion") long id,@RequestBody QuestionDto questionDto) {
        try {
            iQuestionService.updateQuestion(id,questionDto);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The question has been successfully updated!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
    @PutMapping("${question.delete}")
    public ResponseEntity<ApiResponse<String>> deleteQuestion(@RequestParam(name="idQuestion") Long id) {
        try {
            iQuestionService.deleteQuestion(id);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The question has been successfully deleted!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }






}
