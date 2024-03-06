package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(scope = CourseDto.class, generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class CourseDto {

    private long id;
    private String name;
    private String backgroundImage;
    private String description;
    private List<CourseLessonDto> lessons;
    private List<AbilityCourseDto> abilities;
    private LocalDateTime createDate;
    private LocalDateTime lastModified;
    private String createdBy;
    private String lastModifiedBy;
    private String imageResource;



}
