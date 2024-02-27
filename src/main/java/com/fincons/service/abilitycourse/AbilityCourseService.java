package com.fincons.service.abilitycourse;

import com.fincons.mapper.AbilityCourseMapper;
import com.fincons.mapper.AbilityUserMapper;
import com.fincons.repository.AbilityCourseRepository;
import com.fincons.repository.AbilityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbilityCourseService implements IAbilityCourseService{

    @Autowired
    private AbilityCourseRepository abilityCourseRepository;

    @Autowired
    private AbilityCourseMapper abilityCourseMapper;
}
