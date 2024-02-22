package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private long id;

    private String name;

    @JsonIgnoreProperties("roles")
    private List<UserDto> users;

}
