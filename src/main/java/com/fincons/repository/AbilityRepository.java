package com.fincons.repository;

import com.fincons.entity.Ability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbilityRepository extends JpaRepository<Ability, Long> {

    Ability findByName(String nameOfAbility);

    boolean existsByName(String name);

    boolean existsByNameIgnoreCase(String name);

    Ability findByNameIgnoreCase(String name);

    List<Ability> findAllByDeletedFalse();//1.Ability

    Ability findByNameAndDeletedFalse(String name);//2.Ability

    boolean existsByIdAndDeletedFalse(long id);

    Ability findByIdAndDeletedFalse(long id);

    boolean existsByNameAndDeletedFalse(String name);

    boolean existsByNameAndIdNot(String name, long id);
}
