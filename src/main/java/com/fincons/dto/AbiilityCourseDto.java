package com.fincons.dto;

import com.fincons.entity.Ability;
import com.fincons.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AbiilityCourseDto {

    private long id;
    private Course course;
    private Ability ability;

}
