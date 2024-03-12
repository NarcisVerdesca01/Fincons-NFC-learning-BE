package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(scope = RoleDto.class ,generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class RoleDto {

    private long id;
    private String name;
    private List<UserDto> users;

    private boolean deleted;


}
