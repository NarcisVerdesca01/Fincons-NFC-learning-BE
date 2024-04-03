package com.fincons.mapper;

import com.fincons.dto.AbilityUserDto;
import com.fincons.entity.AbilityUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AbilityUserMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public AbilityUserDto mapAbilityUserToAbilityUserDto(AbilityUser abilityUser){
        return modelMapper.map(abilityUser, AbilityUserDto.class);
    }

    public AbilityUser mapDtoToAbilityUser(AbilityUserDto abilityUserDto){
        return modelMapper.map(abilityUserDto, AbilityUser.class);
    }

    public List<AbilityUserDto> mapAbilityUserListToAbilityUserDtoList(List<AbilityUser> abilityUserList) {
        return abilityUserList.stream()
                .map(this::mapAbilityUserToAbilityUserDto)
                .collect(Collectors.toList());
    }

    public List<AbilityUser> mapAbilityUserDtoListToAbilityUserList(List<AbilityUserDto> abilityUserDtoList) {
        return abilityUserDtoList.stream()
                .map(this::mapDtoToAbilityUser)
                .collect(Collectors.toList());
    }


}
