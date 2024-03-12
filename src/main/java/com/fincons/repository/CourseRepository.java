package com.fincons.repository;

import com.fincons.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {

    boolean existsByName(String name);

    Course findByName(String name);


    boolean existsByNameAndIdNot(String name, long id);

    List<Course> findAllByDeletedFalse();

    boolean existsByNameAndDeletedFalse(String name);

    boolean existsByIdAndDeletedFalse(long id);

    Course findByIdAndDeletedFalse(long id);

    Course findByNameAndDeletedFalse(String name);
}
