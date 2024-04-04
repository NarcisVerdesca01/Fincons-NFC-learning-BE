package com.fincons.controller;

import com.fincons.dto.AnswerDto;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AnswerMapper;
import com.fincons.service.answer.IAnswerService;
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

    @GetMapping("${answer.get-all-answer-noassociationquestion}")
    public ResponseEntity<ApiResponse<List<AnswerDto>>> getAllAnswerWithoutAssociationWithQuestion(){
        List<AnswerDto> answerDtoList= iAnswerService.findAllAnswerWithoutQuestion()
                .stream()
                .map(s->answerMapper.mapAnswerToAnswerDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<AnswerDto>>builder()
                .data(answerDtoList)
                .build());
    }
    @GetMapping("${answer.get-by-id}")
    public ResponseEntity<ApiResponse<AnswerDto>> getById(@RequestParam(name="idAnswer") long id){
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
        } catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.badRequest().body(ApiResponse.<AnswerDto>builder()
                    .message(illegalArgumentException.getMessage())
                    .build());
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<AnswerDto>builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PutMapping("${answer.update}")
    public ResponseEntity<ApiResponse<String>> updateAnswer(@RequestParam(name="idAnswer") long id,@RequestBody AnswerDto answerDto) {
        try {
            iAnswerService.updateAnswer(id,answerDto);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The answer has been successfully updated!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }catch (IllegalArgumentException illegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.<String>builder()
                    .message(illegalArgumentException.getMessage())
                    .build());
        }
    }

    @PutMapping("${answer.delete}")
    public ResponseEntity<ApiResponse<String>> deleteAnswer(@RequestParam(name="idAnswer") long id) {
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


    @PutMapping("${answer.associate.question}")
    public ResponseEntity<ApiResponse<String>> associateQuestionToAnswer(@RequestParam(name="idAnswer")long idAnswer,@RequestParam long idQuestion) {
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<String>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }


}
