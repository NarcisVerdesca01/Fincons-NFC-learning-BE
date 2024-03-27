package com.fincons.controller.ability;

import com.fincons.controller.AbilityController;
import com.fincons.dto.AbilityDto;
import com.fincons.dto.AnswerDto;
import com.fincons.entity.Ability;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityMapper;
import com.fincons.service.ability.IAbilityService;
import com.fincons.utility.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class AbilityControllerTest {

    @Autowired
    private AbilityController abilityController;
    @MockBean
    private IAbilityService iAbilityService;
    @Autowired
    private AbilityMapper abilityMapper;


    @Test
    void testGetAbilityByName_Success(){
        Ability ability = new Ability(1L,"Informatica",null,null,false);
        when(iAbilityService.findAbilityByName("Informatica")).thenReturn(ability);
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.getAbilityByName("Informatica");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AbilityDto abilityDto = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(abilityDto);
        assertEquals(1, abilityDto.getId());
        assertEquals(ability.getId(), abilityDto.getId());
        assertEquals(ability.getName(), abilityDto.getName());
        assertEquals(ability.isDeleted(),abilityDto.isDeleted());
    }

    @Test
    void testGetAllAbilities_Success() {
        List<Ability> abilities = Arrays.asList(new Ability(1L, "Ability1", null, null,false),
                new Ability(2L, "Ability2", null, null,false),
                new Ability(3L, "Ability2", null, null,true)
        );
        when(iAbilityService.findAllAbilities()).thenReturn(abilities
                        .stream()
                        .filter(a-> !a.isDeleted())
                        .toList()
                        );
        ResponseEntity<ApiResponse<List<AbilityDto>>> responseEntity = abilityController.getAllAbilities();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<AbilityDto> responseAbilities = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(responseAbilities);
        assertEquals(2, responseAbilities.size());
        assertEquals("Ability1", responseAbilities.get(0).getName());
        assertEquals("Ability2", responseAbilities.get(1).getName());
        assertFalse( responseAbilities.get(0).isDeleted());
        assertFalse(responseAbilities.get(1).isDeleted());
    }


    @Test
    void testGetAbilityById_Success() throws DuplicateException {
        Ability ability = new Ability(1L, "Informatica",null,null,false);
        when(iAbilityService.findAbilityById(1L)).thenReturn(ability);
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.getAbilityById(abilityMapper.mapAbilityToAbilityDto(ability).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getData());
        assertEquals(ability.getId(), responseEntity.getBody().getData().getId());
        assertEquals(ability.getName(), responseEntity.getBody().getData().getName());
        assertFalse(responseEntity.getBody().getData().isDeleted());
    }

    @Test
    void testGetAnswerById_ResourceNotFoundSuccess(){
        long nonExistingAbilityId = 999L;
        doThrow(new ResourceNotFoundException("The ability does not exist")).when(iAbilityService).findAbilityById(nonExistingAbilityId);
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.getAbilityById(nonExistingAbilityId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The ability does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
     void testGetAbilityByName_ResourceNotFoundException() throws DuplicateException {
        String nonExistingAbilityName = "nonExistingAbilityName";
        doThrow(new ResourceNotFoundException("The ability does not exist")).when(iAbilityService).findAbilityByName(nonExistingAbilityName);
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.getAbilityByName(nonExistingAbilityName);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(Objects.requireNonNull(responseEntity.getBody()).getData());
        assertEquals("The ability does not exist", responseEntity.getBody().getMessage());
    }

    @Test
    void testCreateAbility_Success() throws DuplicateException {
        AbilityDto inputAbilityDto = new AbilityDto(1L,"Informatica",null,null,false);
        when(iAbilityService.createAbility(inputAbilityDto)).thenReturn(abilityMapper.mapDtoToAbility(inputAbilityDto));
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.createAbility(inputAbilityDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputAbilityDto.getName(), Objects.requireNonNull(responseEntity.getBody()).getData().getName());
        assertFalse(responseEntity.getBody().getData().isDeleted());
    }

    @Test
    void testCreateAbility_InvalidInput() throws DuplicateException {
        AbilityDto invalidAbilityDto = new AbilityDto();
        when(iAbilityService.createAbility(invalidAbilityDto)).thenThrow(new IllegalArgumentException("The name of ability can't be empty"));
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.createAbility(invalidAbilityDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("The name of ability can't be empty", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testCreateAbility_Duplicate() throws DuplicateException {
        AbilityDto inputAbilityDto = new AbilityDto();
        when(iAbilityService.createAbility(inputAbilityDto)).thenThrow(new DuplicateException("The name of ability already exists"));
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.createAbility(inputAbilityDto);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("The name of ability already exists", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testUpdateAbility_Success() throws DuplicateException {
        long abilityId = 1L;
        AbilityDto inputAbilityDto = new AbilityDto(1L,"Programmazione",null,null,false);
        Ability updatedAbility = new Ability(1L,"Programmazione",null,null,false);
        when(iAbilityService.updateAbility(abilityId, inputAbilityDto)).thenReturn(updatedAbility);
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.updateAbility(abilityId, inputAbilityDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedAbility.getName(), Objects.requireNonNull(responseEntity.getBody()).getData().getName());
        assertFalse(responseEntity.getBody().getData().isDeleted());
    }

    @Test
    void testUpdateAbility_ResourceNotFound() throws DuplicateException {
        long nonExistingAbilityId = 999L;
        AbilityDto inputAbilityDto = new AbilityDto();
        when(iAbilityService.updateAbility(nonExistingAbilityId, inputAbilityDto))
                .thenThrow(new ResourceNotFoundException("Ability does not exist"));
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.updateAbility(nonExistingAbilityId, inputAbilityDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Ability does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testUpdateAbility_Duplicate() throws DuplicateException {
        long abilityId = 1L;
        AbilityDto inputAbilityDto = new AbilityDto();
        when(iAbilityService.updateAbility(abilityId, inputAbilityDto))
                .thenThrow(new DuplicateException("Ability already exists!"));
        ResponseEntity<ApiResponse<AbilityDto>> responseEntity = abilityController.updateAbility(abilityId, inputAbilityDto);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Ability already exists!", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testDeleteAbility_Success() {
        Ability abilityToDelete = new Ability(1L,"randomName",null,null,false);
        ResponseEntity<ApiResponse<String>> responseEntity = abilityController.deleteAbility(abilityToDelete.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The ability has been successfully deleted!", Objects.requireNonNull(responseEntity.getBody()).getData());
        verify(iAbilityService).deleteAbility(abilityToDelete.getId()); // Verify that the service method was called
    }

    @Test
    void testDeleteAbility_ResourceNotFound() {
        long nonExistingAbilityId = 999L;
        doThrow(new ResourceNotFoundException("The ability does not exist")).when(iAbilityService).deleteAbility(nonExistingAbilityId);
        ResponseEntity<ApiResponse<String>> responseEntity = abilityController.deleteAbility(nonExistingAbilityId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The ability does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }


}
