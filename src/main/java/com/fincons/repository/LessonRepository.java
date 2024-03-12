package com.fincons.repository;

import com.fincons.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    boolean existsByTitle(String title);

    boolean existsByTitleIgnoreCase(String title);

    List<Lesson> findAllByDeletedFalse();

    boolean existsByIdAndDeletedFalse(long id);

    Lesson findByIdAndDeletedFalse(long id);

    boolean existsByTitleAndDeletedFalse(String title);

    boolean existsByTitleAndIdNot(String title, long id);
}
