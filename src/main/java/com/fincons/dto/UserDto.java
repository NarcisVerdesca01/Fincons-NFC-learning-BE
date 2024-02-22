package com.fincons.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @JsonIgnoreProperties("users")
    private List<RoleDto> roles;

    private boolean generatedPassword;
}
