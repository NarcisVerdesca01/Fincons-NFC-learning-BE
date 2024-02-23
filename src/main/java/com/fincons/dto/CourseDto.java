package com.fincons.dto;


import com.fincons.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

    private String name;

    private String description;

    private String requirementsAccess;

    private List<Lesson> lessons;

}
