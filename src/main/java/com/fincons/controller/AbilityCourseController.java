package com.fincons.controller;

import com.fincons.dto.AbilityCourseDto;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityCourseMapper;
import com.fincons.service.abilitycourse.IAbilityCourseService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
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
public class AbilityCourseController {

    private IAbilityCourseService iAbilityCourseService;

    private AbilityCourseMapper abilityCourseMapper;

    @GetMapping("${ability-course.list}")
    public ResponseEntity<ApiResponse<List<AbilityCourseDto>>> getAllAbilityCourse(){

        List<AbilityCourseDto> abilityCourseDtos = abilityCourseMapper
                .mapAbilityCourseListToAbilityCourseDtoList(iAbilityCourseService.getAllAbilityCourse());
        return ResponseEntity.ok().body(ApiResponse.<List<AbilityCourseDto>>builder()
                .data(abilityCourseDtos)
                .build());
    }

    @GetMapping("${ability-course.find-by-id}/{id}")
    public ResponseEntity<ApiResponse<AbilityCourseDto>> getAbilityCourseById(@PathVariable long id){

        try{
            AbilityCourseDto abilityCourseDto = abilityCourseMapper
                    .mapAbilityCourseToAbilityCourseDto(iAbilityCourseService.getAbilityCourseById(id));
            return ResponseEntity.ok().body(ApiResponse.<AbilityCourseDto>builder()
                    .data(abilityCourseDto)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<AbilityCourseDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }

    }




    @PostMapping("${ability-course.add}")
    public ResponseEntity<ApiResponse<AbilityCourseDto>> addAbilityCourse(@RequestBody AbilityCourseDto abilityCourseDto ) throws DuplicateException {
        try{
            AbilityCourseDto abilityCourseDtoToShow = abilityCourseMapper
                    .mapAbilityCourseToAbilityCourseDto(iAbilityCourseService.addAbilityCourse(abilityCourseDto));
            return ResponseEntity.ok().body(ApiResponse.<AbilityCourseDto>builder()
                    .data(abilityCourseDtoToShow)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.badRequest().body(ApiResponse.<AbilityCourseDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }catch (DuplicateException duplicateException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<AbilityCourseDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }


    @PutMapping("${ability-course.update}/{id}")
    public ResponseEntity<ApiResponse<AbilityCourseDto>> updateAbilityCourse(@PathVariable long id, @RequestBody AbilityCourseDto abilityCourseDto) {
        try{
            AbilityCourseDto abilityCourseDtoToShow =
                    abilityCourseMapper.mapAbilityCourseToAbilityCourseDto(iAbilityCourseService.updateAbilityCourse(id,abilityCourseDto));

            return ResponseEntity.ok().body(ApiResponse.<AbilityCourseDto>builder()
                    .data(abilityCourseDtoToShow)
                    .build());

        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<AbilityCourseDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        } catch (DuplicateException duplicateException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<AbilityCourseDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }

    @PutMapping("${ability-course.delete}/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAbilityCourse(@PathVariable long id)  {
        try{
            iAbilityCourseService.deleteAbilityCourse(id);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .message("Deleted relationship between ability and course chose")
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
}
