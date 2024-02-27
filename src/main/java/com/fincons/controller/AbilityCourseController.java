package com.fincons.controller;

import com.fincons.mapper.AbilityCourseMapper;
import com.fincons.mapper.AbilityUserMapper;
import com.fincons.service.abilitycourse.IAbilityCourseService;
import com.fincons.service.abilityuser.IAbilityUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("${application.context}")
public class AbilityCourseController {

    private IAbilityCourseService iAbilityCourseService;

    private AbilityCourseMapper abilityCourseMapper;


}
