package com.fincons.controller;

import com.fincons.dto.UserDto;
import com.fincons.exception.UserDataException;
import com.fincons.jwt.JwtAuthResponse;
import com.fincons.jwt.LoginDto;
import com.fincons.service.authorization.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class AuthController {

    private IAuthService IAuthService;

    @PostMapping("${register.student.uri}") //register student
    public ResponseEntity<String> registerStudent(
            @RequestBody UserDto userDto
    ) throws UserDataException {

        try{
            String response = IAuthService.registerStudent(userDto);
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
            String response = IAuthService.registerTutor(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(UserDataException userDataException){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDataException.getMessage());
        }

    }



    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){

            String token = IAuthService.login(loginDto);
            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setAccessToken(token);

            return  ResponseEntity.status(HttpStatus.OK).body(jwtAuthResponse);
    }







}
