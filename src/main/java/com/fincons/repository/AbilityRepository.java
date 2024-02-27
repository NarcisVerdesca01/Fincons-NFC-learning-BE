package com.fincons.repository;

import com.fincons.entity.Ability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbilityRepository extends JpaRepository<Ability, Long> {

    Ability findByName(String nameOfAbility);

    boolean existsByName(String name);
}
