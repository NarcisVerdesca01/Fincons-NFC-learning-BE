package com.fincons.mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fincons.dto.AbilityDto;
import com.fincons.entity.Ability;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbilityMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public AbilityDto mapAbilityToAbilityDto(Ability ability){
        return modelMapper.map(ability, AbilityDto.class);
    }

    public Ability mapDtoToAbility(AbilityDto abilityDto){
        return modelMapper.map(abilityDto, Ability.class);
    }

}
