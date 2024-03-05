package com.fincons.controller;

import com.fincons.dto.AbilityDto;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityMapper;
import com.fincons.service.ability.IAbilityService;
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
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<AbilityDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }

    }

    @GetMapping("${ability.get-by-id}/{id}")
    public ResponseEntity<ApiResponse<AbilityDto>> getAbilityById(@PathVariable("id") long id) {
        try{
            AbilityDto abilityDto = abilityMapper.mapAbilityToAbilityDto( iAbilityService.findAbilityById(id));
            return ResponseEntity.ok().body(ApiResponse.<AbilityDto>builder()
                    .data(abilityDto)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<AbilityDto>builder()
                    .message(resourceNotFoundException.getMessage())
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
        }catch(IllegalArgumentException illegalArgumentException){
            return ResponseEntity.badRequest().body(ApiResponse.<AbilityDto>builder()
                            .message(illegalArgumentException.getMessage())
                    .build());
        }catch(DuplicateException duplicateException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<AbilityDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }

    @PutMapping("${ability.update}/{id}")
    public ResponseEntity<ApiResponse<AbilityDto>> updateAbility(@PathVariable long id, @RequestBody AbilityDto abilityDto) {
        try {
            AbilityDto updatedAbilityDto = abilityMapper.mapAbilityToAbilityDto(iAbilityService.updateAbility(id, abilityDto));
            return ResponseEntity.ok().body(ApiResponse.<AbilityDto>builder()
                    .data(updatedAbilityDto)
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<AbilityDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }catch(DuplicateException duplicateException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<AbilityDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }

    @DeleteMapping("${ability.delete}/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAbility(@PathVariable long id) {
        try {
            iAbilityService.deleteAbility(id);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The ability has been successfully deleted!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }



}
