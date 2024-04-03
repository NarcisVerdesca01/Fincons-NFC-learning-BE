package com.fincons.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fincons.dto.QuizResultsDto;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.QuizResultMapper;
import com.fincons.service.quizresult.IQuizResultService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class QuizResultController {


    private IQuizResultService iQuizResultService;

    private QuizResultMapper quizResultMapper;

    private static final Logger LOG = LoggerFactory.getLogger(QuizResultController.class);

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

    @GetMapping("${quiz-result-student.list.singleStudent}")
    public ResponseEntity<ApiResponse<List<QuizResultsDto>>> getQuizResultAboutSingleStudent() {
        List<QuizResultsDto> quizResultsDtosList= iQuizResultService.findQuizResultAboutSingleStudent()
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

    @GetMapping(value = "${quiz-result-student.check}")
    public ResponseEntity<ApiResponse<Boolean>> checkDoneQuiz(
            @RequestParam("quizId") long quizId) {

        try {
            boolean results = iQuizResultService.checkIfAlreadyDone(quizId);

            LOG.info("Done quiz checked successfully by: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(),LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<Boolean>builder()
                    .data(results)
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {

            LOG.error("ResourceNotFoundException - checkDoneQuiz() -> QuizResultController: {}. Date: {}", resourceNotFoundException.getMessage(), LocalDateTime.now());
            return ResponseEntity.badRequest().body(ApiResponse.<Boolean>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }

    @PostMapping(value = "${quiz-result-student.calculate}")
    public ResponseEntity<ApiResponse<QuizResultsDto>> calculateAndSave(
            @RequestParam("quizId") long quizId,
            @RequestBody Map<String, Object> requestBody) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<Long, List<Long>> answersMap = objectMapper.convertValue(requestBody.get("answersMap"), new TypeReference<>() {
            });
            QuizResultsDto results = quizResultMapper
                    .mapQuizResultsEntityToDto(iQuizResultService.calculateAndSave(quizId, answersMap)) ;

            LOG.info("Quiz results calculated and saved successfully by: {}. Date: {}",SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<QuizResultsDto>builder()
                    .data(results)
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {

            LOG.error("ResourceNotFoundException - calculateAndSave() -> QuizResultController: {}. Date: {}", resourceNotFoundException.getMessage(), LocalDateTime.now());
            return ResponseEntity.badRequest().body(ApiResponse.<QuizResultsDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }catch (DuplicateException duplicateException) {

            LOG.error("DuplicateException - calculateAndSave() -> QuizResultController: {}. Date: {}", duplicateException.getMessage(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<QuizResultsDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }

    @PutMapping(value = "${quiz-result-student.redo}")
    public ResponseEntity<ApiResponse<QuizResultsDto>> reDoQuiz(
            @RequestParam("quizToRedo") long quizToRedo,
            @RequestBody Map<String, Object> requestBody) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Map<Long, List<Long>> answersMap = objectMapper.convertValue(requestBody.get("answersMap"), new TypeReference<Map<Long, List<Long>>>(){});
            QuizResultsDto results = quizResultMapper
                    .mapQuizResultsEntityToDto(iQuizResultService.redoQuiz(quizToRedo, answersMap)) ;

            LOG.info("Quiz redone successfully by: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<QuizResultsDto>builder()
                    .data(results)
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {

            LOG.error("ResourceNotFoundException - reDoQuiz() -> QuizResultController: {}. Date: {}", resourceNotFoundException.getMessage(), LocalDateTime.now());
            return ResponseEntity.badRequest().body(ApiResponse.<QuizResultsDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }


}
