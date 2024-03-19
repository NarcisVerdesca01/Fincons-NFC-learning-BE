package com.fincons.controller;

import com.fincons.dto.AbilityDto;
import com.fincons.dto.AbilityUserDto;
import com.fincons.entity.Ability;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.jwt.JwtTokenProvider;
import com.fincons.mapper.AbilityUserMapper;
import com.fincons.repository.AbilityRepository;
import com.fincons.repository.UserRepository;
import com.fincons.service.abilityuser.IAbilityUserService;
import com.fincons.utility.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class AbilityUserController {

    private IAbilityUserService iAbilityUserService;
    private AbilityUserMapper abilityUserMapper;
    private JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;
    private AbilityRepository abilityRepository;


    @GetMapping("${ability-user.list}")
    public ResponseEntity<ApiResponse<List<AbilityUserDto>>> getAllAbilityUser(){

        List<AbilityUserDto> abilityUserDtos = abilityUserMapper
                .mapAbilityUserListToAbilityUserDtoList(iAbilityUserService.getAllAbilityUser());
        return ResponseEntity.ok().body(ApiResponse.<List<AbilityUserDto>>builder()
                .data(abilityUserDtos)
                .build());
    }

    @GetMapping("${ability-user.get-by-id}/{id}")
    public ResponseEntity<ApiResponse<AbilityUserDto>> getAbilityUserById(@PathVariable long id){
        try{
            AbilityUserDto abilityUserDto = abilityUserMapper
                    .mapAbilityUserToAbilityUserDto(iAbilityUserService.getAbilityUserById(id));
            return ResponseEntity.ok().body(ApiResponse.<AbilityUserDto>builder()
                    .data(abilityUserDto)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<AbilityUserDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }

    }


    @PostMapping("${ability-user.add}")
    public ResponseEntity<ApiResponse<AbilityUserDto>> addAbilityUser(
            @RequestParam(name = "abilityId") long abilityIdToAssociate
            ) {
        try {

            AbilityUserDto abilityUserDtoToShow = abilityUserMapper
                    .mapAbilityUserToAbilityUserDto(iAbilityUserService.addAbilityUser(abilityIdToAssociate));
            return ResponseEntity.ok().body(ApiResponse.<AbilityUserDto>builder()
                    .data(abilityUserDtoToShow)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.badRequest().body(ApiResponse.<AbilityUserDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }catch (DuplicateException duplicateException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<AbilityUserDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }

    @PutMapping("${ability-user.update}/{id}")
    public ResponseEntity<ApiResponse<AbilityUserDto>> updateAbilityUser(@PathVariable long id, @RequestBody AbilityUserDto abilityUserDto)  {
        try{
            AbilityUserDto abilityUserDtoToShow =
                    abilityUserMapper.mapAbilityUserToAbilityUserDto(iAbilityUserService.updateAbilityUser(id,abilityUserDto));

            return ResponseEntity.ok().body(ApiResponse.<AbilityUserDto>builder()
                    .data(abilityUserDtoToShow)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<AbilityUserDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        } catch (DuplicateException duplicateException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.<AbilityUserDto>builder()
                    .message(duplicateException.getMessage())
                    .build());
        }
    }

    @PutMapping("${ability-user.delete}")
    public ResponseEntity<ApiResponse<String>> deleteAbilityUser(@RequestParam long id) {
        try{
            iAbilityUserService.deleteAbilityUser(id);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .message("Deleted relationship between ability  and user chosen")
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }


}
