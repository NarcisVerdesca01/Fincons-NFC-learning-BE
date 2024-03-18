package com.fincons.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fincons.dto.UserDto;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.exception.UserDataException;
import com.fincons.jwt.JwtAuthResponse;
import com.fincons.jwt.JwtUnauthorizedAuthenticationEntryPoint;
import com.fincons.jwt.LoginDto;
import com.fincons.mapper.UserAndRoleMapper;
import com.fincons.service.authorization.IAuthService;
import com.fincons.utility.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class AuthController {


    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private IAuthService iAuthService;

    private UserAndRoleMapper userAndRoleMapper;

    @PostMapping("${register.student.uri}") //register student
    public ResponseEntity<String> registerStudent(
            @RequestBody UserDto userDto
    ) throws UserDataException {

        try{
            String response = iAuthService.registerStudent(userDto);

            LOG.info("Student successfully registered: {}", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(UserDataException userDataException){

            LOG.error("Error while registering Student: {}", userDataException.getMessage());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDataException.getMessage());
        }

    }

    @PostMapping("${register.tutor.uri}") //register tutor
    public ResponseEntity<String> registerTutor(
            @RequestBody UserDto userDto
    ) throws UserDataException {

        try{
            String response = iAuthService.registerTutor(userDto);
            LOG.info("Tutor successfully registered: {}", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(UserDataException userDataException){
            LOG.error("Error while registering Tutor: {}", userDataException.getMessage());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDataException.getMessage());
        }

    }

    @PostMapping("${register.admin.uri}") //register tutor
    public ResponseEntity<String> registerAdmin(
            @RequestBody UserDto userDto
    ) throws UserDataException {

        try{
            String response = iAuthService.registerAdmin(userDto);
            LOG.info("Admin successfully registered: {}", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch(UserDataException userDataException){
            LOG.error("Admin successfully registered: {}", userDto.getEmail());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDataException.getMessage());
        }

    }



    @PostMapping("${login.uri}")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto) {
        try {
            String token = iAuthService.login(loginDto);
            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setAccessToken(token);
            LOG.info("User logged in successfully: {}", loginDto.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(jwtAuthResponse);
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtAuthResponse("Bad credentials!"));
        } catch (org.springframework.security.access.AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new JwtAuthResponse("Access denied!"));
        }
    }



    @GetMapping("${detail.userdto}")
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail() {
        try{
            UserDto userDTO = userAndRoleMapper.userToUserDto(iAuthService.getUserByEmail());
            return ResponseEntity.status(HttpStatus.OK).body(
                    ApiResponse.<UserDto>builder()
                            .data(userDTO)
                            .build());
        }catch (ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.<UserDto>builder()
                            .message(resourceNotFoundException.getMessage())
                            .build());
        }
    }









}
