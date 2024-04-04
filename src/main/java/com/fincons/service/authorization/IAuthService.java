package com.fincons.service.authorization;

import com.fincons.dto.UserDto;
import com.fincons.entity.User;
import com.fincons.exception.UserDataException;
import com.fincons.jwt.LoginDto;

public interface IAuthService {

    String registerStudent(UserDto userDto) throws UserDataException;

    String login(LoginDto loginDto) ;

    String registerTutor(UserDto userDto) throws UserDataException;

    String registerAdmin(UserDto userDto) throws UserDataException;

    User getUserByEmail();

    String updateUser(UserDto userDto) throws UserDataException;

}
