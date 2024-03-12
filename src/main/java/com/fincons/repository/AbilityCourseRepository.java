package com.fincons.repository;

import com.fincons.entity.Ability;
import com.fincons.entity.AbilityCourse;
import com.fincons.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AbilityCourseRepository extends JpaRepository<AbilityCourse,Long> {


    boolean existsByAbilityAndCourse(Ability ability, Course course);


    List<AbilityCourse> findAllByDeletedFalse();


    boolean existsByAbilityAndCourseAndDeletedFalse(Ability existingAbility, Course existingCourse);

    AbilityCourse findByIdAndDeletedFalse(long id);

    boolean existsByIdAndDeletedFalse(long id);
}
