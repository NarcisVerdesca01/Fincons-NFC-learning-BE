package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fincons.entity.Ability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Data
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

    private List<AbilityUserDto> abilities;


}
