package com.fincons.controller;


import com.fincons.dto.AbilityDto;
import com.fincons.dto.CourseDto;
import com.fincons.mapper.AbilityMapper;
import com.fincons.service.ability.IAbilityService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class AbilityController {

    private IAbilityService iAbilityService;

    private AbilityMapper abilityMapper;

    @GetMapping("${ability.list}")
    public ResponseEntity<ApiResponse<List<AbilityDto>>> getAllAbilities(){
        List<AbilityDto> abilitiesDtoList= iAbilityService.findAllAbilities()
                .stream()
                .map(a->abilityMapper.mapAbilityToAbilityDto(a))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<AbilityDto>>builder()
                .data(abilitiesDtoList)
                .build());
    }

}
