package com.fincons.controller;

import com.fincons.dto.AnswerDto;
import com.fincons.dto.ContentDto;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AnswerMapper;
import com.fincons.mapper.ContentMapper;
import com.fincons.service.answer.IAnswerService;
import com.fincons.service.content.IContentService;
import com.fincons.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin("*")
@RequestMapping("${application.context}")
public class AnswerController {

    @Autowired
    private IAnswerService iAnswerService;
    @Autowired
    private AnswerMapper answerMapper;

    @GetMapping("${answer.get-all-answer}")
    public ResponseEntity<ApiResponse<List<AnswerDto>>> getAllAnswer(){
        List<AnswerDto> answerDtoList= iAnswerService.findAllAnswer()
                .stream()
                .map(s->answerMapper.mapAnswerToAnswerDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<AnswerDto>>builder()
                .data(answerDtoList)
                .build());
    }
    @GetMapping("${answer.get-by-id}/{id}")
    public ResponseEntity<ApiResponse<AnswerDto>> getById(@PathVariable long id){
        try{
            AnswerDto answerDto= answerMapper.mapAnswerToAnswerDto(iAnswerService.findById(id));
            return ResponseEntity.ok().body(ApiResponse.<AnswerDto>builder()
                    .data(answerDto)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<AnswerDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
    @PostMapping("${answer.create}")
    public ResponseEntity<ApiResponse<AnswerDto>> createAnswer(@RequestBody AnswerDto answerDto) {
        try {
            AnswerDto answerDtoToShow = answerMapper.mapAnswerToAnswerDto(iAnswerService.createAnswer(answerDto));
            return ResponseEntity.ok().body(ApiResponse.<AnswerDto>builder()
                    .data(answerDtoToShow)
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(ApiResponse.<AnswerDto>builder()
                    .message(exception.getMessage())
                    .build());
        }
    }

    @PutMapping("${answer.update}/{id}")
    public ResponseEntity<ApiResponse<String>> updateAnswer(@PathVariable long id,@RequestBody AnswerDto answerDto) {
        try {
            iAnswerService.updateAnswer(id,answerDto);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The answer has been successfully updated!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
    @DeleteMapping("${answer.delete}/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAnswer(@PathVariable long id) {
        try {
            iAnswerService.deleteAnswer(id);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The answer has been successfully deleted!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }


    @PutMapping("${answer.associate.question}/{idAnswer}/{idQuestion}")
    public ResponseEntity<ApiResponse<String>> associateQuestionToAnswer(@PathVariable long idAnswer,@PathVariable long idQuestion) {
        try {
            iAnswerService.associateQuestionToAnswer(idAnswer,idQuestion);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The question has been successfully associated with answer!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        } catch (DuplicateException duplicateException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }


}
