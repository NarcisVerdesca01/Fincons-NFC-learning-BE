package com.fincons.controller;

import com.fincons.dto.QuizDto;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.QuizMapper;
import com.fincons.service.quiz.IQuizService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class QuizController {

    @Autowired
    private IQuizService iQuizService;
    @Autowired
    private QuizMapper quizMapper;
    private static final Logger LOG = LoggerFactory.getLogger(QuizController.class);


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

    @GetMapping("${quiz.get-all-quiz-noassociationlesson}")
    public ResponseEntity<ApiResponse<List<QuizDto>>> getAllQuizWithoutAssociationWithLesson(){
        List<QuizDto> questionDtoList= iQuizService.findAllQuizWithoutLesson()
                .stream()
                .map(s->quizMapper.mapQuizToQuizDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<QuizDto>>builder()
                .data(questionDtoList)
                .build());
    }

    @GetMapping("${quiz.get-all-quiz-noassociationquestion}")
    public ResponseEntity<ApiResponse<List<QuizDto>>> getAllQuizWithoutAssociationWithQuestions(){
        List<QuizDto> questionDtoList= iQuizService.findAllQuizWithoutQuestions()
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
            QuizDto quizDto= quizMapper.mapQuizToQuizDto(iQuizService.findById(id));
            return ResponseEntity.ok().body(ApiResponse.<QuizDto>builder()
                    .data(quizDto)
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
            QuizDto quizDtoToShow = quizMapper.mapQuizToQuizDto(iQuizService.createQuiz(quizDto));

            LOG.info("Quiz created successfully by: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<QuizDto>builder()
                    .data(quizDtoToShow)
                    .build());
        } catch (DuplicateException exception) {
            LOG.error("DuplicateException - createQuiz() -> QuizController. Date: {}", LocalDateTime.now());
            return ResponseEntity.badRequest().body(ApiResponse.<QuizDto>builder()
                    .message(exception.getMessage())
                    .build());
        }
    }

    @PutMapping("${quiz.update}/{id}")
    public ResponseEntity<ApiResponse<String>> updateQuiz(@PathVariable long id,@RequestBody QuizDto quizDto) {
        try {
            iQuizService.updateQuiz(id,quizDto);

            LOG.info("Quiz updated successfully with ID: {} by: {}. Date: {}", id, SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The quiz has been successfully updated!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {

            LOG.error("DuplicateException - updateQuiz() -> QuizController. Date: {}", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
    @PutMapping("${quiz.delete}")
    public ResponseEntity<ApiResponse<String>> deleteQuiz(@RequestParam long id) {
        try {
            iQuizService.deleteQuiz(id);

            LOG.info("Quiz deleted successfully with ID: {} by: {}. Date: {}", id, SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The quiz has been successfully deleted!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {

            LOG.error("ResourceNotFoundException - deleteQuiz() -> QuizController. Date: {}",  LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }


    @PutMapping("${quiz.associate.lesson}")
    public ResponseEntity<ApiResponse<String>> associateQuizLesson(@RequestParam long idQuiz,@RequestParam long idLesson) {
        try {
            iQuizService.associateLesson(idQuiz,idLesson);

            LOG.info("Quiz associated with lesson successfully. Quiz ID: {}, Lesson ID: {} by: {}. Date: {}", idQuiz, idLesson, SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The quiz has been successfully associated with lesson!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {

            LOG.warn("ResourceNotFoundException - associateQuizLesson() -> QuizController: {}. Date: {}", resourceNotFoundException.getMessage(),  LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        } catch (DuplicateException duplicateException){

            LOG.error("DuplicateException - associateQuizLesson() -> QuizController: {}. Date: {}", duplicateException.getMessage(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<String>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }


    @PutMapping("${quiz.associate.question}")
    public ResponseEntity<ApiResponse<String>> associateQuizQuestion(@RequestParam long idQuiz,@RequestParam long idQuestion) {
        try {
            iQuizService.associateQuestion(idQuiz,idQuestion);

            LOG.info("Quiz associated with question successfully. Quiz ID: {}, Question ID: {} by: {}. Date: {}", idQuiz, idQuestion, SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The quiz has been successfully associated with question!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {

            LOG.error("ResourceNotFoundException - associateQuizQuestion() -> QuizController: {}. Date: {}", resourceNotFoundException.getMessage(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        } catch (DuplicateException duplicateException){

            LOG.error("DuplicateException - associateQuizQuestion() -> QuizController: {}. Date: {}", duplicateException.getMessage(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<String>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }





}
