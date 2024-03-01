package com.fincons.repository;

import com.fincons.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    boolean existsByTitle(String title);

    boolean existsByTitleIgnoreCase(String title);
}
