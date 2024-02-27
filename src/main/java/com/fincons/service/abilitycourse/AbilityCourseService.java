package com.fincons.service.abilitycourse;

import com.fincons.dto.AbilityCourseDto;
import com.fincons.dto.AbilityUserDto;
import com.fincons.entity.AbilityCourse;
import com.fincons.entity.AbilityUser;
import com.fincons.mapper.AbilityCourseMapper;
import com.fincons.mapper.AbilityUserMapper;
import com.fincons.repository.AbilityCourseRepository;
import com.fincons.repository.AbilityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbilityCourseService implements IAbilityCourseService{

    @Autowired
    private AbilityCourseRepository abilityCourseRepository;

    @Autowired
    private AbilityCourseMapper abilityCourseMapper;

    @Override
    public List<AbilityCourse> getAllAbilityCourse() {
        return abilityCourseRepository.findAll();
    }

    @Override
    public AbilityCourse addAbilityCourse(AbilityCourseDto abilityCourseDto) {
        return abilityCourseRepository.save(abilityCourseMapper.mapDtoToAbilityCourse(abilityCourseDto)) ;
    }
}
