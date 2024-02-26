package com.fincons.controller;

import com.fincons.dto.CourseDto;
import com.fincons.exception.CourseException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.CourseMapper;
import com.fincons.service.course.ICourseService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("${course.create}")
    public ResponseEntity<ApiResponse<CourseDto>> createCourse(@RequestBody CourseDto courseDto) {
        try {
            CourseDto courseDtoToShow = courseMapper.mapCourseToCourseDto(iCourseService.createCourse(courseDto));
            return ResponseEntity.ok().body(ApiResponse.<CourseDto>builder()
                            .data(courseDtoToShow)
                    .build());
        } catch (CourseException courseException) {
            return ResponseEntity.badRequest().body(ApiResponse.<CourseDto>builder()
                            .message(courseException.getMessage())
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

    //TODO TESTING THIS ON POSTMAN
    @DeleteMapping("${course.delete}/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCourse(@PathVariable long id) {
        try {
            iCourseService.deleteCourse(id);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The course has been successfully deleted!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }

    // TODO SERVIZIO CHE RESTITUISCE I corsi in

    // base all'utente.  Parametri necessari:  id_user loggato.
    // tramite id accedo al ruolo:
    // - se ruolo è admin allora vedi tutti i corsi
    // - se ruolo è studente o tutor accedi alle ability e vedi quelle che matchano con i corsi

    //TODO per testarlo serve 1. creare un user di tipo o studente o tutor o admin con
    // abilità. 2.creare un corso con abilità  user e corso devono avere le stesse abilità e non per testare
    // che se matcha restituisce i corsi dell'user se non matcha non li restituisce e se è admin li restituisce tutti

    @GetMapping ("${course.getDedicatedCourses}/{email}")
    public ResponseEntity<ApiResponse<List<CourseDto>>> getDedicatedCourses(@PathVariable String email){

        List<CourseDto> coursesDtoList= iCourseService.findDedicatedCourses(email)
                .stream()
                .map(c->courseMapper.mapCourseToCourseDto(c))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<CourseDto>>builder()
                .data(coursesDtoList)
                .build());
    }

    /*
     @PutMapping("${course.update}/{id}")
    public ResponseEntity<ApiResponse<CourseDto>> updateCourse(@PathVariable long id, @RequestBody CourseDto courseDto) {
        try {
            CourseDto updatedCourseDto = courseMapper.mapCourseToCourseDto(iCourseService.updateCourse(id, courseDto));
            return ResponseEntity.ok().body(ApiResponse.<CourseDto>builder()
                    .data(updatedCourseDto)
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<CourseDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
     */





}
