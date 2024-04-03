package com.fincons.controller;

import com.fincons.dto.CourseDto;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.exception.UserDataException;
import com.fincons.jwt.JwtTokenProvider;
import com.fincons.mapper.CourseMapper;
import com.fincons.service.course.ICourseService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CourseController {

    @Autowired
    private ICourseService iCourseService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);

    @GetMapping("${course.get-all-courses}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getAllCourses(){
       List<CourseDto> coursesDtoList= iCourseService.findAllCourses()
               .stream()
               .map(c->courseMapper.mapCourseToCourseDto(c))
               .toList();
            return ResponseEntity.ok().body(ApiResponse.<List<CourseDto>>builder()
                    .data(coursesDtoList)
                    .build());
    }

    @GetMapping("${course.get-all-courses-noassociatedlesson}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getAllCoursesWithoutLesson(){
        List<CourseDto> coursesDtoList= iCourseService.findAllCoursesWithoutLesson()
                .stream()
                .map(c->courseMapper.mapCourseToCourseDto(c))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<CourseDto>>builder()
                .data(coursesDtoList)
                .build());
    }

    @PostMapping("${course.create}")
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(@RequestBody CourseDto courseDto) {
        try {
            CourseDto courseDtoToShow = courseMapper.mapCourseToCourseDto(iCourseService.createCourse(courseDto));

            LOG.info("Course created successfully by: {}. Course name: '{}', Date: {}", courseDtoToShow.getCreatedBy(), courseDtoToShow.getName(), courseDtoToShow.getCreateDate());
            return ResponseEntity.ok().body(ApiResponse.<CourseDto>builder()
                            .data(courseDtoToShow)
                    .build());
        } catch (IllegalArgumentException illegalArgumentException) {
            LOG.error("IllegalArgumentException - createCourse() -> CourseController. Failed for: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.badRequest().body(ApiResponse.<CourseDto>builder()
                            .message(illegalArgumentException.getMessage())
                    .build());
        }catch(DuplicateException duplicateException){
            LOG.error("DuplicateException - createCourse() -> CourseController. Failed for: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<CourseDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }

    @GetMapping("${course.getById}/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> getCourseById(@PathVariable long id){
        try{
            CourseDto courseDtoToShow = courseMapper.mapCourseToCourseDto(iCourseService.findCourseById(id));
            return ResponseEntity.ok().body(ApiResponse.<CourseDto>builder()
                            .data(courseDtoToShow)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<CourseDto>builder()
                            .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }

    @GetMapping("${course.getByName}/{name}")
    public ResponseEntity<ApiResponse<CourseDto>> getCourseByName(@PathVariable String name){
        try{
            CourseDto courseDtoToShow = courseMapper.mapCourseToCourseDto(iCourseService.findCourseByName(name));
            return ResponseEntity.ok().body(ApiResponse.<CourseDto>builder()
                    .data(courseDtoToShow)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<CourseDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }

    @PutMapping("${course.delete}")
    public ResponseEntity<ApiResponse<String>> deleteCourse(@RequestParam long id) {
        try {
            iCourseService.deleteCourse(id);
            LOG.info("Course deleted successfully by: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The course has been successfully deleted!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            LOG.error("ResourceNotFoundException - deleteCourse() -> CourseController");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }

    @GetMapping ("${course.getDedicatedCourses}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getDedicatedCourses() throws UserDataException {
        try{
            List<CourseDto> coursesDtoList= iCourseService.findDedicatedCourses()
                    .stream()
                    .map(c->courseMapper.mapCourseToCourseDto(c))
                    .toList();

            return ResponseEntity.ok().body(ApiResponse.<List<CourseDto>>builder()
                    .data(coursesDtoList)
                    .build());

        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<List<CourseDto>>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }

     @PutMapping("${course.update}/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> updateCourse(@PathVariable long id, @RequestBody CourseDto courseDto) {
        try {
            CourseDto updatedCourseDto = courseMapper.mapCourseToCourseDto(iCourseService.updateCourse(id, courseDto));

            LOG.info("Course updated successfully by: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.ok().body(ApiResponse.<CourseDto>builder()
                    .data(updatedCourseDto)
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            LOG.error("ResourceNotFoundException - updateCourse() -> CourseController. Failed for: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<CourseDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        } catch (DuplicateException duplicateException) {
            LOG.error("DuplicateException - updateCourse() -> CourseController. Failed for: {}. Date: {}", SecurityContextHolder.getContext().getAuthentication().getName(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<CourseDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
     }


}
