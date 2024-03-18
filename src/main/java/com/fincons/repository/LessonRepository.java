package com.fincons.repository;

import com.fincons.entity.Content;
import com.fincons.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    boolean existsByTitle(String title);

    boolean existsByTitleIgnoreCase(String title);

    List<Lesson> findAllByDeletedFalse();

    List<Lesson> findAllByContentIsNullAndDeletedFalse();

    boolean existsByIdAndDeletedFalse(long id);

    Lesson findByIdAndDeletedFalse(long id);

    boolean existsByTitleAndDeletedFalse(String title);

    boolean existsByTitleAndIdNot(String title, long id);

    boolean existsByContent(Content content);
}
