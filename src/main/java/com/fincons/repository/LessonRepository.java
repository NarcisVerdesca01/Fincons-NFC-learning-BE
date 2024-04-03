package com.fincons.repository;

import com.fincons.entity.Content;
import com.fincons.entity.Lesson;
import com.fincons.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    boolean existsByTitle(String title);

    boolean existsByTitleIgnoreCase(String title);

    List<Lesson> findAllByDeletedFalse();

    List<Lesson> findAllByContentIsNullAndDeletedFalse();

    List<Lesson> findAllByQuizIsNullAndDeletedFalse();

    List<Lesson> findAllByCourseLessonsIsNullAndDeletedFalse();

    boolean existsByIdAndDeletedFalse(long id);

    Lesson findByIdAndDeletedFalse(long id);

    boolean existsByTitleAndDeletedFalse(String title);

    boolean existsByTitleAndIdNot(String title, long id);

    boolean existsByContent(Content content);

    Lesson findByContent(Content content);

    Lesson findByQuiz(Quiz quizToDelete);


}
