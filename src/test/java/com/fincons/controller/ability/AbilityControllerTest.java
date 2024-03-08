package com.fincons.controller.ability;

import com.fincons.controller.AbilityController;
import com.fincons.dto.AbilityDto;
import com.fincons.entity.Ability;
import com.fincons.mapper.AbilityMapper;
import com.fincons.repository.AbilityRepository;
import com.fincons.service.ability.AbilityService;
import com.fincons.service.ability.IAbilityService;
import com.fincons.utility.ApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AbilityControllerTest {


    @Autowired
    private AbilityController abilityController;

    @MockBean
    private AbilityRepository  abilityRepository;

    @Autowired
    private AbilityMapper abilityMapper;


    @Test
    public void testGetAbilityByName_Success(){
        Ability ability = new Ability(1L,"Informatica",null,null);

        when(abilityRepository.findByName("Informatica")).thenReturn(ability);

        // Call the controller method
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.getAbilityByName("Informatica");

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AbilityDto abilityDto = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(abilityDto);
        assertEquals(1, abilityDto.getId());

        assertEquals(ability.getId(), abilityDto.getId());
        assertEquals(ability.getName(), abilityDto.getName());

    }

    @Test
    public void testGetAllAbility_Success(){
        Ability ability = new Ability(1L,"Informatica",null,null);

        when(abilityRepository.findByName("Informatica")).thenReturn(ability);

        // Call the controller method
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.getAbilityByName("Informatica");

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AbilityDto abilityDto = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(abilityDto);
        assertEquals(1, abilityDto.getId());

        assertEquals(ability.getId(), abilityDto.getId());
        assertEquals(ability.getName(), abilityDto.getName());

    }

    @Test
    public void testGetAllAbilities_Success() {
        // Setup
        List<Ability> abilities = Arrays.asList(new Ability(1L, "Ability1", null, null),
                new Ability(2L, "Ability2", null, null));
        when(abilityRepository.findAll()).thenReturn(abilities);

        List<AbilityDto> abilityDtos = Arrays.asList(new AbilityDto(1L, "Ability1",null,null),
                new AbilityDto(2L, "Ability2",null,null));

        ResponseEntity<ApiResponse<List<AbilityDto>>> responseEntity = abilityController.getAllAbilities();

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<AbilityDto> responseAbilities = responseEntity.getBody().getData();
        assertNotNull(responseAbilities);
        assertEquals(2, responseAbilities.size());
        assertEquals("Ability1", responseAbilities.get(0).getName());
        assertEquals("Ability2", responseAbilities.get(1).getName());
    }



}
