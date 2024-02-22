package com.fincons.service;

import com.fincons.dto.RegisterDto;
import com.fincons.dto.UserDto;

public interface AuthService {
    String register(RegisterDto registerDto);
}
