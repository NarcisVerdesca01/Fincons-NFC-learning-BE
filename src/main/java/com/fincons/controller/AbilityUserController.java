package com.fincons.controller;


import com.fincons.dto.AbilityDto;
import com.fincons.dto.AbilityUserDto;
import com.fincons.entity.AbilityUser;
import com.fincons.mapper.AbilityUserMapper;
import com.fincons.service.abilityuser.IAbilityUserService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class AbilityUserController {

    private IAbilityUserService iAbilityUserService;

    private AbilityUserMapper abilityUserMapper;

    @GetMapping("${ability-user.list}")
    public ResponseEntity<ApiResponse<List<AbilityUserDto>>> getAllAbilityUser(){

        List<AbilityUserDto> abilityUserDtos = abilityUserMapper
                .mapAbilityUserListToAbilityUserDtoList(iAbilityUserService.getAllAbilityUser());
        return ResponseEntity.ok().body(ApiResponse.<List<AbilityUserDto>>builder()
                .data(abilityUserDtos)
                .build());
    }

    @PostMapping("${ability-user.add}")
    public ResponseEntity<ApiResponse<AbilityUserDto>> addAbilityUser(@RequestBody AbilityUserDto abilityUserDto ){
        AbilityUserDto abilityUserDtoToShow = abilityUserMapper
                .mapAbilityUserToAbilityUserDto(iAbilityUserService.addAbilityUser(abilityUserDto));
        return ResponseEntity.ok().body(ApiResponse.<AbilityUserDto>builder()
                .data(abilityUserDtoToShow)
                .build());
    }

    //TODO DELETE
    //TODO UPDATE

}
