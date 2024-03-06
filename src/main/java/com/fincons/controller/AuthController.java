package com.fincons.controller;

import com.fincons.dto.UserDto;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.exception.UserDataException;
import com.fincons.jwt.JwtAuthResponse;
import com.fincons.jwt.LoginDto;
import com.fincons.mapper.UserAndRoleMapper;
import com.fincons.service.authorization.AuthService;
import com.fincons.service.authorization.IAuthService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class AuthController {


    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    private IAuthService iAuthService;

    private UserAndRoleMapper userAndRoleMapper;

    @PostMapping("${register.student.uri}") //register student
    public ResponseEntity<String> registerStudent(
            @RequestBody UserDto userDto
    ) throws UserDataException {

        try{
            String response = iAuthService.registerStudent(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(UserDataException userDataException){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDataException.getMessage());
        }

    }

    @PostMapping("${register.tutor.uri}") //register tutor
    public ResponseEntity<String> registerTutor(
            @RequestBody UserDto userDto
    ) throws UserDataException {

        try{
            String response = iAuthService.registerTutor(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(UserDataException userDataException){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDataException.getMessage());
        }

    }

    @PostMapping("${register.admin.uri}") //register tutor
    public ResponseEntity<String> registerAdmin(
            @RequestBody UserDto userDto
    ) throws UserDataException {

        try{
            String response = iAuthService.registerAdmin(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(UserDataException userDataException){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDataException.getMessage());
        }

    }



    @PostMapping("${login.uri}")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){

            String token = iAuthService.login(loginDto);
            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setAccessToken(token);
            return  ResponseEntity.status(HttpStatus.OK).body(jwtAuthResponse);

    }

    @GetMapping("${detail.userdto}")
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail(@RequestParam(value = "email") String email) {
        try{
            UserDto userDTO = userAndRoleMapper.userToUserDto(iAuthService.getUserByEmail(email));
            LOG.info("User info: " + userDTO.getRoles().get(0).getName());

            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponse.<UserDto>builder()
                            .data(userDTO)
                            .build());
        }catch (ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponse.<UserDto>builder()
                            .message(resourceNotFoundException.getMessage())
                            .build());
        }
    }









}
