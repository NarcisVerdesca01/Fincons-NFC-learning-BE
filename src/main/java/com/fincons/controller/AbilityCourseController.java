package com.fincons.controller;

import com.fincons.dto.AbilityCourseDto;
import com.fincons.dto.AbilityUserDto;
import com.fincons.mapper.AbilityCourseMapper;
import com.fincons.service.abilitycourse.IAbilityCourseService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
/*
    @GetMapping("${ability-course.list}")
    public ResponseEntity<ApiResponse<List<AbilityCourseDto>>> getAllAbilityCourse(){

        List<AbilityCourseDto> abilityCourseDtos = abilityCourseMapper
                .mapAbilityCourseListToAbilityCourseDtoList(iAbilityCourseService.getAllAbilityCourse());
        return ResponseEntity.ok().body(ApiResponse.<List<AbilityCourseDto>>builder()
                .data(abilityCourseDtos)
                .build());
    }

    @PostMapping("${ability-course.add}")
    public ResponseEntity<ApiResponse<AbilityCourseDto>> addAbilityCourse(@RequestBody AbilityCourseDto abilityCourseDto ){
        AbilityCourseDto abilityCourseDtoToShow = abilityCourseMapper
                .mapAbilityCourseToAbilityCourseDto(iAbilityCourseService.addAbilityCourse(abilityCourseDto));
        return ResponseEntity.ok().body(ApiResponse.<AbilityCourseDto>builder()
                .data(abilityCourseDtoToShow)
                .build());
    }

 */

    //TODO DELETE
    //TODO UPDATE

}
