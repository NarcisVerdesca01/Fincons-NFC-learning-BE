package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime lastModified;
    private String createdBy;
    private String lastModifiedBy;


}
