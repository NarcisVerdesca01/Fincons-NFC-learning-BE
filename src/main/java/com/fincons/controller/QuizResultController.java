package com.fincons.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fincons.dto.QuizResultsDto;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.QuizResultMapper;
import com.fincons.service.quizresult.IQuizResultService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class QuizResultController {


    private IQuizResultService iQuizResultService;

    private QuizResultMapper quizResultMapper;


    @GetMapping("${quiz-result-student.list}")
    public ResponseEntity<ApiResponse<List<QuizResultsDto>>> getAllQuizResults(){
        List<QuizResultsDto> quizResultsDtosList= iQuizResultService.findAllResultsQuiz()
                .stream()
                .map(a->quizResultMapper.mapQuizResultsEntityToDto(a))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<QuizResultsDto>>builder()
                .data(quizResultsDtosList)
                .build());
    }


    @GetMapping("${quiz-result-student.find-by-id}/{id}")
    public ResponseEntity<ApiResponse<QuizResultsDto>> getQuizResultsStudentDtoById(@PathVariable long id){
        try{
            QuizResultsDto quizResultsDto = quizResultMapper
                    .mapQuizResultsEntityToDto(iQuizResultService.getQuizResultsById(id));
            return ResponseEntity.ok().body(ApiResponse.<QuizResultsDto>builder()
                    .data(quizResultsDto)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<QuizResultsDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }



    @PostMapping(value = "${quiz-result-student.calculate}")
    public ResponseEntity<ApiResponse<QuizResultsDto>> calculateAndSave(
            @RequestParam("quizId") long quizId,
            @RequestParam("userEmail") String userEmail,//TODO decode JWT Token (Payload)
            @RequestBody Map<String, Object> request) {


        ObjectMapper objectMapper = new ObjectMapper();
        Map<Long, List<Long>> answersMap = objectMapper.convertValue(request.get("answersMap"), new TypeReference<Map<Long, List<Long>>>(){});

        try {
            QuizResultsDto results = quizResultMapper
                    .mapQuizResultsEntityToDto(iQuizResultService.calculateAndSave(quizId, userEmail, answersMap)) ;

            return ResponseEntity.ok().body(ApiResponse.<QuizResultsDto>builder()
                    .data(results)
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.badRequest().body(ApiResponse.<QuizResultsDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }





}
