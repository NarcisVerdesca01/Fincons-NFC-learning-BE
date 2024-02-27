package com.fincons.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fincons.dto.RoleDto;
import com.fincons.dto.UserDto;
import com.fincons.entity.Role;
import com.fincons.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAndRoleMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User dtoToUser(UserDto userDTO) {
        User userToSave = modelMapper.map(userDTO, User.class);
        userToSave.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userToSave;
    }

    public UserDto userToUserDto(User user) {
        UserDto userDTO = modelMapper.map(user, UserDto.class);
        userDTO.setRoles(user.getRoles().stream().map(this::roleToRoleDto).collect(Collectors.toSet()));
        return userDTO;
    }


    public RoleDto roleToRoleDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }
    public Role dtoToRole(RoleDto roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }
}
