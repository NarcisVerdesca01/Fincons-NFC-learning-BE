package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @JsonIgnoreProperties("course")
    private List<CourseLessonDto> courseLessons;

    @JsonIgnoreProperties("course")
    private List<AbilityCourseDto> abilityCourses;

    private String imageResource;

    private boolean deleted;

    private LocalDateTime createDate;

    private LocalDateTime lastModified;

    private String createdBy;

    private String lastModifiedBy;

}
