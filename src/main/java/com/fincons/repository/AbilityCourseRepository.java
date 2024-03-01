package com.fincons.repository;

import com.fincons.entity.Ability;
import com.fincons.entity.AbilityCourse;
import com.fincons.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbilityCourseRepository extends JpaRepository<AbilityCourse,Long> {


    boolean existsByAbilityAndCourse(Ability ability, Course course);
}
