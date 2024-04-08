package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

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
    private List<RoleDto> roles;

    @JsonIgnoreProperties("user")
    private List<AbilityUserDto> abilityUsers;

    @JsonIgnoreProperties("user")
    //@JsonManagedReference
    private List<QuizResultsDto> quizResults;

    private boolean deleted;

    private String backgroundImage;

}
