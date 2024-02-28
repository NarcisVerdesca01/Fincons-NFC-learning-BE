package com.fincons.repository;

import com.fincons.entity.Course;
import com.fincons.entity.CourseLesson;
import com.fincons.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseLessonRepository extends JpaRepository<CourseLesson,Long> {
    boolean existsByCourseAndLesson(Course course, Lesson lesson);
}
