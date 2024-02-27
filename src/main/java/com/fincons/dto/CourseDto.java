package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fincons.entity.AbilityCourse;
import com.fincons.entity.CourseLesson;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

    private long id;

    private String name;

    private String description;

    private List<CourseLessonDto> lessons;

    private List<AbilityCourseDto> abilities;

    private LocalDateTime createDate;

    private LocalDateTime lastModified;

    private String createdBy;

    private String lastModifiedBy;


}
