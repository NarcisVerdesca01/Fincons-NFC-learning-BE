package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class QuizDto {

    private long id;

    private String title;

    @JsonIgnoreProperties("quiz")
    private List<QuestionDto> questions;

    @JsonIgnoreProperties("quiz")
    private LessonDto lesson;

    @JsonIgnoreProperties("quiz")
    //@JsonManagedReference
    private List<QuizResultsDto> quizResults;

    private boolean deleted;

    private LocalDateTime createDate;

    private LocalDateTime lastModified;

    private String createdBy;

    private String lastModifiedBy;

}
