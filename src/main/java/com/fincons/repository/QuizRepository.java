package com.fincons.repository;

import com.fincons.entity.Lesson;
import com.fincons.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    boolean existsByTitle(String title);
    boolean existsByLesson(Lesson lesson);

    boolean existsByIdAndDeletedFalse(long id);

    Quiz findByIdAndDeletedFalse(long id);

    List<Quiz> findAllByDeletedFalse();

    List<Quiz> findAllByDeletedFalseAndLessonIsNull();

    List<Quiz> findAllByDeletedFalseAndQuestionsIsNull();
    boolean existsByTitleAndDeletedFalse(String title);
}
