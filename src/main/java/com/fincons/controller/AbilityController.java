package com.fincons.controller;


import com.fincons.dto.AbilityDto;
import com.fincons.exception.AbilityException;
import com.fincons.mapper.AbilityMapper;
import com.fincons.service.ability.IAbilityService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("${ability.get-by-name}/{name}")
    public ResponseEntity<ApiResponse<AbilityDto>> getAbilityByName(@PathVariable("name") String name) {
        try{
            AbilityDto abilityDto = abilityMapper.mapAbilityToAbilityDto( iAbilityService.findAbilityByName(name));
            return ResponseEntity.ok().body(ApiResponse.<AbilityDto>builder()
                    .data(abilityDto)
                    .build());
        }catch(AbilityException abilityException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<AbilityDto>builder()
                    .message(abilityException.getMessage())
                    .build());
        }

    }

    @PostMapping("${ability.create}")
    public ResponseEntity<ApiResponse<AbilityDto>> createAbility(@RequestBody AbilityDto abilityDto){
        try {
            AbilityDto abilityDtoToShow = abilityMapper.mapAbilityToAbilityDto(iAbilityService.createAbility(abilityDto));
            return ResponseEntity.ok().body(ApiResponse.<AbilityDto>builder()
                    .data(abilityDtoToShow)
                    .build());
        }catch( AbilityException abilityException){
            return ResponseEntity.badRequest().body(ApiResponse.<AbilityDto>builder()
                            .message(abilityException.getMessage())
                    .build());
        }

    }
    //TODO UPDATE
    //TODO DELETE



}
