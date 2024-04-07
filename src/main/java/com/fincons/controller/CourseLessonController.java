package com.fincons.controller;

import com.fincons.dto.CourseLessonDto;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.CourseLessonMapper;
import com.fincons.service.courselesson.ICourseLessonService;
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

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class CourseLessonController {

    private ICourseLessonService iCourseLessonService;

    private CourseLessonMapper courseLessonMapper;

    private static final Logger LOG = LoggerFactory.getLogger(CourseLessonController.class);

    @GetMapping("${course-lesson.list}")
    public ResponseEntity<ApiResponse<List<CourseLessonDto>>> getAllCourseLesson(){

        List<CourseLessonDto> courseLessonDtos = courseLessonMapper
                .mapCourseLessonListToCourseLessonDtoList(iCourseLessonService.getCourseLessonList()) ;
        return ResponseEntity.ok().body(ApiResponse.<List<CourseLessonDto>>builder()
                .data(courseLessonDtos)
                .build());
    }

    @PostMapping("${course-lesson.add}")
    public ResponseEntity<ApiResponse<CourseLessonDto>> addCourseLesson(@RequestBody CourseLessonDto courseLessonDto) {
        try{
            CourseLessonDto courseLessonDtoToShow = courseLessonMapper
                    .mapCourseLessonEntityToDto(iCourseLessonService.addCourseLesson(courseLessonDto));

            LOG.info("Course lesson relationship added successfully. Author: {}. Course lesson ID: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), courseLessonDtoToShow.getId(), LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<CourseLessonDto>builder()
                    .data(courseLessonDtoToShow)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){

            LOG.error("ResourceNotFoundException - addCourseLesson() -> CourseLessonController. Author: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<CourseLessonDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }catch (DuplicateException duplicateException){
            LOG.error("DuplicateException - addCourseLesson() -> CourseLessonController. Author: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(),LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<CourseLessonDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }

    @PutMapping("${course-lesson.update}/{id}")
    public ResponseEntity<ApiResponse<CourseLessonDto>> updateCourseLesson(@PathVariable long id, @RequestBody CourseLessonDto courseLessonDto){
        try{
            CourseLessonDto courseLessonDtoToShow =
                    courseLessonMapper.mapCourseLessonEntityToDto(iCourseLessonService.updateCourseLesson(id,courseLessonDto));

            LOG.info("Course lesson relationship updated successfully. Author: {}. Course lesson ID: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), courseLessonDtoToShow.getId(), LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<CourseLessonDto>builder()
                    .data(courseLessonDtoToShow)
                    .build());

        }catch(ResourceNotFoundException resourceNotFoundException ){

            LOG.error("ResourceNotFoundException - updateCourseLesson() -> CourseLessonController. Author: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<CourseLessonDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        } catch (DuplicateException duplicateException) {

            LOG.error("DuplicateException - updateCourseLesson() -> CourseLessonController. Author: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<CourseLessonDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }

    @PutMapping("${course-lesson.delete}")
    public ResponseEntity<ApiResponse<String>> deleteCourseLesson(@RequestParam long id) {
        try{
            iCourseLessonService.deleteCourseLesson(id);

            LOG.info("Course lesson relationship deleted successfully. Author: {}. Course lesson ID: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), id, LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .message("Deleted relationship between course and lesson chosen")
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){

            LOG.error("ResourceNotFoundException - deleteCourseLesson() -> CourseLessonController. Author: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }

    @GetMapping("${course-lesson.find-by-id}/{id}")
    public ResponseEntity<ApiResponse<CourseLessonDto>> getCourseLessonById(@PathVariable long id){

        try{
            CourseLessonDto courseLessonDtoToShow = courseLessonMapper
                    .mapCourseLessonEntityToDto(iCourseLessonService.getCourseLessonById(id));
            return ResponseEntity.ok().body(ApiResponse.<CourseLessonDto>builder()
                    .data(courseLessonDtoToShow)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<CourseLessonDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }

    }


}
