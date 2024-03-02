package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class CourseDto {

    private long id;

    private String name;

    private String backgroundImage;

    private String description;

    @JsonIgnore
    private List<CourseLessonDto> lessons;

    @JsonIgnore
    private List<AbilityCourseDto> abilities;

    private LocalDateTime createDate;

    private LocalDateTime lastModified;

    private String createdBy;

    private String lastModifiedBy;




}
