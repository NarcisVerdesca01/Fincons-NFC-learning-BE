package com.fincons.controller;

import com.fincons.dto.LessonDto;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.ContentMapper;
import com.fincons.mapper.LessonMapper;
import com.fincons.service.lesson.ILessonService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class LessonController {


    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private ILessonService iLessonService;

    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private ContentMapper contentMapper;

    @GetMapping("${lesson.get-all-lessons}")
    public ResponseEntity<ApiResponse<List<LessonDto>>> getAllLessons() {
        List<LessonDto> lessonDtoList = iLessonService.findAllLessons()
                .stream()
                .map(s -> lessonMapper.mapLessonToLessonDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<LessonDto>>builder()
                .data(lessonDtoList)
                .build());
    }


    @GetMapping("${lesson.getById}/{id}")
    public ResponseEntity<ApiResponse<LessonDto>> getLessonById(@PathVariable long id){
        try{
            LessonDto lessonDtoToShow = lessonMapper.mapLessonToLessonDto(iLessonService.findLessonById(id));
            return ResponseEntity.ok().body(ApiResponse.<LessonDto>builder()
                    .data(lessonDtoToShow)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<LessonDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }

    @PostMapping("${lesson.add}")
    public ResponseEntity<ApiResponse<LessonDto>> createLesson(@RequestBody LessonDto lessonDto){
        try {
            LessonDto lessonDtoToShow = lessonMapper.mapLessonToLessonDto(iLessonService.createLesson(lessonDto));
            LOG.info("{} successfully registered new lesson  ' {} ' , When: {} ", SecurityContextHolder.getContext().getAuthentication().getName(), lessonDtoToShow.getTitle(), lessonDtoToShow.getCreateDate());
            return ResponseEntity.ok().body(ApiResponse.<LessonDto>builder()
                    .data(lessonDtoToShow)
                    .build());
        } catch (IllegalArgumentException illegalArgumentException) {
            LOG.info("IllegalArgumentException - createLesson() -> LessonController");
            return ResponseEntity.badRequest().body(ApiResponse.<LessonDto>builder()
                            .message(illegalArgumentException.getMessage())
                    .build());
        } catch (DuplicateException e) {
            LOG.info("DuplicateException - createLesson() -> LessonController");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<LessonDto>builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @PutMapping("${lesson.update}/{id}")
    public ResponseEntity<ApiResponse<LessonDto>> updateLesson(@PathVariable long id, @RequestBody LessonDto lessonDto) {
        try {
            LessonDto updatedLessonDto = lessonMapper.mapLessonToLessonDto(iLessonService.updateLesson(id, lessonDto));
            LOG.info("{} successfully updated lesson  ' {} ' , When: {} ", updatedLessonDto.getLastModifiedBy(), updatedLessonDto.getTitle(),  LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<LessonDto>builder()
                    .data(updatedLessonDto)
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            LOG.info("ResourceNotFoundException - updateLesson() -> LessonController");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<LessonDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }catch (DuplicateException duplicateException) {
            LOG.info("DuplicateException - updateLesson() -> LessonController");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<LessonDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }

    @PutMapping("${lesson.delete}/{id}")
    public ResponseEntity<ApiResponse<String>> deleteLesson(@PathVariable long id) {
        try {
            iLessonService.deleteLesson(id);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The lesson has been successfully deleted!")
                    .build());
        } catch (ResourceNotFoundException  resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }

    @PutMapping("${lesson.associate.content}")
    public ResponseEntity<ApiResponse<LessonDto>> associateContentToLesson(
            @RequestParam(value = "lesson") long lessonId , @RequestParam(value = "content") long contentId) {

        try {
            LessonDto updatedLessonDto = lessonMapper.mapLessonToLessonDto(
                    iLessonService.associateContentToLesson(lessonId,contentId));

            return ResponseEntity.ok().body(ApiResponse.<LessonDto>builder()
                    .data(updatedLessonDto)
                    .build());
        } catch (ResourceNotFoundException  resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<LessonDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }catch (IllegalArgumentException  illegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.<LessonDto>builder()
                    .message(illegalArgumentException.getMessage())
                    .build());
        }catch (DuplicateException  duplicateException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<LessonDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }catch(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<LessonDto>builder()
                    .message(sqlIntegrityConstraintViolationException.getMessage())
                    .build());
        }
    }


}
