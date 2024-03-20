package com.fincons.controller.abilityuser;

import com.fincons.controller.AbilityUserController;
import com.fincons.dto.AbilityDto;
import com.fincons.dto.AbilityUserDto;
import com.fincons.dto.UserDto;
import com.fincons.entity.Ability;
import com.fincons.entity.AbilityUser;
import com.fincons.entity.User;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityUserMapper;
import com.fincons.service.abilityuser.IAbilityUserService;
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
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AbilityUserControllerTest {


    @Autowired
    private AbilityUserController abilityUserController;

    @MockBean
    private IAbilityUserService iAbilityUserService;

    @Autowired
    private AbilityUserMapper abilityUserMapper;


    @Test
    public void testGetAllAbilityUser_Success() {
        List<AbilityUser> abilityCourseList = getAbilityUserListData();
        when(iAbilityUserService.getAllAbilityUser()).thenReturn(abilityCourseList
                .stream()
                .filter(au->!au.isDeleted())
                .toList());
        ResponseEntity<ApiResponse<List<AbilityUserDto>>> responseEntity = abilityUserController.getAllAbilityUser();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getData());
        assertEquals(2, responseEntity.getBody().getData().size());
        assertFalse( responseEntity.getBody().getData().get(0).isDeleted());
        assertFalse( responseEntity.getBody().getData().get(1).isDeleted());
    }

    @Test
    public void testGetAbilityUserById_Success() {
        Ability ability1 = new Ability();
        User user1 = new User();
        AbilityUser abilityUser = new AbilityUser(1L,user1,ability1,false);
        when(iAbilityUserService.getAbilityUserById(1L)).thenReturn(abilityUser);
        ResponseEntity<ApiResponse<AbilityUserDto>> responseEntity = abilityUserController.getAbilityUserById(abilityUserMapper.mapAbilityUserToAbilityUserDto(abilityUser).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(abilityUser.getId(), Objects.requireNonNull(responseEntity.getBody()).getData().getId());
        assertFalse(responseEntity.getBody().getData().isDeleted());
    }

    @Test
    public void testGetAbilityUserById_ResourceNotFound(){
        long nonExistingAbilityUserId = 1L;
        doThrow(new ResourceNotFoundException("The ability-user association does not exist")).when(iAbilityUserService).getAbilityUserById(nonExistingAbilityUserId);
        ResponseEntity<ApiResponse<AbilityUserDto>> responseEntity = abilityUserController.getAbilityUserById(nonExistingAbilityUserId);
        assertEquals("The ability-user association does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    @Test
    public void testCreateAbilityUser_Success() throws DuplicateException {
        AbilityDto abilityDto = new AbilityDto();
        UserDto userDto = new UserDto();
        AbilityUserDto abilityUserDto = new AbilityUserDto(1L,userDto,abilityDto,false);
        when(iAbilityUserService.addAbilityUser(1L)).thenReturn(abilityUserMapper.mapDtoToAbilityUser(abilityUserDto));
        ResponseEntity<ApiResponse<AbilityUserDto>> responseEntity = abilityUserController.addAbilityUser(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(abilityUserDto.getAbility().getId(), Objects.requireNonNull(responseEntity.getBody()).getData().getAbility().getId());
        assertEquals(abilityUserDto.getUser().getId(), Objects.requireNonNull(responseEntity.getBody()).getData().getUser().getId());
        verify(iAbilityUserService).addAbilityUser(1L);
    }

    @Test
    public void testCreateAbilityUser_ResourceNotFoundException() throws DuplicateException {
        AbilityDto abilityDto = new AbilityDto();
        UserDto userDto = new UserDto();
        AbilityUserDto abilityUserDto = new AbilityUserDto(1L,userDto,abilityDto,false);
        doThrow(new ResourceNotFoundException("The ability-user association does not exist")).when(iAbilityUserService).addAbilityUser(2L);
        ResponseEntity<ApiResponse<AbilityUserDto>> responseEntity = abilityUserController.addAbilityUser(2L);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The ability-user association does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testUpdateAbilityUser_Success() throws DuplicateException {
        AbilityDto abilityDto = new AbilityDto();
        UserDto userDto = new UserDto();
        AbilityUserDto inputAbilityUserDto = new AbilityUserDto(1L,userDto,abilityDto,false);
        Ability ability1 = new Ability();
        User user1 = new User();
        AbilityUser updatedAbilityUser = new AbilityUser(1L,user1,ability1,false);
        long idOfAbilityUserAssociationToModify = 1L;
        when(iAbilityUserService.updateAbilityUser(idOfAbilityUserAssociationToModify, inputAbilityUserDto)).thenReturn(updatedAbilityUser);
        ResponseEntity<ApiResponse<AbilityUserDto>> responseEntity = abilityUserController.updateAbilityUser(idOfAbilityUserAssociationToModify, inputAbilityUserDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateAbilityUser_ResourceNotFound() throws DuplicateException {
        long nonExistingAbilityUserId = 999L;
        AbilityUserDto inputAbilityUserDto = new AbilityUserDto();
        when(iAbilityUserService.updateAbilityUser(nonExistingAbilityUserId, inputAbilityUserDto))
                .thenThrow(new ResourceNotFoundException("Ability-User association does not exist"));
        ResponseEntity<ApiResponse<AbilityUserDto>> responseEntity = abilityUserController.updateAbilityUser(nonExistingAbilityUserId, inputAbilityUserDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Ability-User association does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testUpdateAbilityUser_Duplicate() throws DuplicateException {
        long abilityUserId = 1L;
        AbilityUserDto inputAbilityUserDto = new AbilityUserDto();
        when(iAbilityUserService.updateAbilityUser(abilityUserId, inputAbilityUserDto))
                .thenThrow(new DuplicateException("Ability-User association already exists!"));
        ResponseEntity<ApiResponse<AbilityUserDto>> responseEntity = abilityUserController.updateAbilityUser(abilityUserId, inputAbilityUserDto);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Ability-User association already exists!", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testDeleteAbilityUserAssociation_Success() {
        Ability ability1 = new Ability();
        User user1 = new User();
        AbilityUser abilityUserToDelete = new AbilityUser(1L, user1, ability1,false);
        ResponseEntity<ApiResponse<String>> responseEntity = abilityUserController.deleteAbilityUser(abilityUserToDelete.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Deleted relationship between ability  and user chosen", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        verify(iAbilityUserService).deleteAbilityUser(abilityUserToDelete.getId());
    }

    @Test
    public void testDeleteAbilityUser_ResourceNotFound() {
        long nonExistingAbilityUserId = 999L;
        doThrow(new ResourceNotFoundException("The ability user association does not exist")).when(iAbilityUserService).deleteAbilityUser(nonExistingAbilityUserId);
        ResponseEntity<ApiResponse<String>> responseEntity = abilityUserController.deleteAbilityUser(nonExistingAbilityUserId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The ability user association does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }



    private List<AbilityUser> getAbilityUserListData() {
        Ability ability1 = new Ability();
        User user1 = new User();
        Ability ability2 = new Ability();
        User user2 = new User();
        AbilityUser abilityUser = new AbilityUser(1L,user1,ability1,false);
        AbilityUser abilityUser2 = new AbilityUser(2L,user2,ability2,false);
        return java.util.Arrays.asList(abilityUser,abilityUser2);
    }

}
