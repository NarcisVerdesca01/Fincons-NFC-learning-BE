package com.fincons.service.impl;

import com.fincons.dto.RegisterDto;
import com.fincons.dto.UserDto;
import com.fincons.entity.User;
import com.fincons.jwt.JwtTokenProvider;
import com.fincons.repository.RoleRepository;
import com.fincons.repository.UserRepository;
import com.fincons.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public String register(RegisterDto registerDto) {

        // check email is already exists in database
        if(userRepository.existsByUsername(registerDto.getEmail())){
            throw new UserDataException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        return null;
    }
}
