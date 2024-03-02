package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private LocalDate birthDate;

    @JsonIgnoreProperties("users")
    private Set<RoleDto> roles;

    @JsonIgnore
    private List<AbilityUserDto> abilities;


}
