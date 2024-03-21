package com.fincons.controller.abilitycourse;

import com.fincons.controller.AbilityCourseController;
import com.fincons.dto.AbilityCourseDto;
import com.fincons.dto.AbilityDto;
import com.fincons.dto.CourseDto;
import com.fincons.entity.Ability;
import com.fincons.entity.AbilityCourse;
import com.fincons.entity.Course;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AbilityCourseMapper;
import com.fincons.service.abilitycourse.IAbilityCourseService;
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
public class AbilityCourseTest {


    @Autowired
    private AbilityCourseController abilityCourseController;

    @MockBean
    private IAbilityCourseService iAbilityCourseService;

    @Autowired
    private AbilityCourseMapper abilityCourseMapper;


    @Test
    public void testGetAllAbilityCourse_Success() {
        List<AbilityCourse> abilityCourseList = getAbilityCourseListData();
        when(iAbilityCourseService.getAllAbilityCourse()).thenReturn(abilityCourseList
                .stream()
                .filter(ac->!ac.isDeleted())
                .toList());
        ResponseEntity<ApiResponse<List<AbilityCourseDto>>> responseEntity = abilityCourseController.getAllAbilityCourse();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getData());
        assertEquals(2, responseEntity.getBody().getData().size());
        assertFalse( responseEntity.getBody().getData().get(0).isDeleted());
        assertFalse( responseEntity.getBody().getData().get(1).isDeleted());
    }

    @Test
    public void testGetAbilityCourseById_Success() {
        Ability ability1 = new Ability(1L, "Ability1", null, null,false);
        Course course1 = new Course(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        AbilityCourse abilityCourse1 = new AbilityCourse(1L, course1, ability1,false);
        when(iAbilityCourseService.getAbilityCourseById(1L)).thenReturn(abilityCourse1);
        ResponseEntity<ApiResponse<AbilityCourseDto>> responseEntity = abilityCourseController.getAbilityCourseById(abilityCourseMapper.mapAbilityCourseToAbilityCourseDto(abilityCourse1).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(abilityCourse1.getId(), Objects.requireNonNull(responseEntity.getBody()).getData().getId());
        assertFalse(responseEntity.getBody().getData().isDeleted());

    }

    @Test
    public void testGetAbilityCourseById_ResourceNotFound(){
        long nonExistingAbilityCourseId = 1L;
        doThrow(new ResourceNotFoundException("The ability-course association does not exist")).when(iAbilityCourseService).getAbilityCourseById(nonExistingAbilityCourseId);
        ResponseEntity<ApiResponse<AbilityCourseDto>> responseEntity = abilityCourseController.getAbilityCourseById(nonExistingAbilityCourseId);
        assertEquals("The ability-course association does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    @Test
    public void testCreateAbilityCourse_Success() throws DuplicateException {
        AbilityDto abilityDto = new AbilityDto(1L, "Ability1", null, null,false);
        CourseDto courseDto = new CourseDto(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        AbilityCourseDto inputAbilityCourseAssociationByUser = new AbilityCourseDto(1L, courseDto, abilityDto,false);
        when(iAbilityCourseService.addAbilityCourse(inputAbilityCourseAssociationByUser)).thenReturn(abilityCourseMapper.mapDtoToAbilityCourse(inputAbilityCourseAssociationByUser));
        ResponseEntity<ApiResponse<AbilityCourseDto>> responseEntity = abilityCourseController.addAbilityCourse(inputAbilityCourseAssociationByUser);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputAbilityCourseAssociationByUser.getAbility().getId(), Objects.requireNonNull(responseEntity.getBody()).getData().getAbility().getId());
        assertEquals(inputAbilityCourseAssociationByUser.getCourse().getId(), Objects.requireNonNull(responseEntity.getBody()).getData().getCourse().getId());
        verify(iAbilityCourseService).addAbilityCourse(inputAbilityCourseAssociationByUser);
    }

    @Test
    public void testCreateAbilityCourse_ResourceNotFoundException() throws DuplicateException {
        AbilityCourseDto invalidAbilityCourseDto = new AbilityCourseDto();
        doThrow(new ResourceNotFoundException("The ability does not exist")).when(iAbilityCourseService).addAbilityCourse(invalidAbilityCourseDto);
        ResponseEntity<ApiResponse<AbilityCourseDto>> responseEntity = abilityCourseController.addAbilityCourse(invalidAbilityCourseDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The ability does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testUpdateAbilityCourse_Success() throws DuplicateException {
        Ability ability = new Ability(1L, "Ability1", null, null,false);
        Course course = new Course(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        AbilityDto abilityDto = new AbilityDto(1L, "Ability1", null, null,false);
        CourseDto courseDto = new CourseDto(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        long idOfAbilityCourseAssociationToModify = 1L;
        AbilityCourseDto inputAbilityCourseDto = new AbilityCourseDto(1L, courseDto, abilityDto,false);
        AbilityCourse updatedAbilityCourse = new AbilityCourse(1L, course, ability,false);
        when(iAbilityCourseService.updateAbilityCourse(idOfAbilityCourseAssociationToModify, inputAbilityCourseDto)).thenReturn(updatedAbilityCourse);
        ResponseEntity<ApiResponse<AbilityCourseDto>> responseEntity = abilityCourseController.updateAbilityCourse(idOfAbilityCourseAssociationToModify, inputAbilityCourseDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedAbilityCourse.getAbility().getName(), Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).getData().getAbility().getName()));
        assertFalse(responseEntity.getBody().getData().isDeleted());
    }

    @Test
    public void testUpdateAbilityCourse_ResourceNotFound() throws DuplicateException {
        long nonExistingAbilityCourseId = 999L;
        AbilityCourseDto inputAbilityCourseDto = new AbilityCourseDto();
        when(iAbilityCourseService.updateAbilityCourse(nonExistingAbilityCourseId, inputAbilityCourseDto))
                .thenThrow(new ResourceNotFoundException("Ability-Course association does not exist"));
        ResponseEntity<ApiResponse<AbilityCourseDto>> responseEntity = abilityCourseController.updateAbilityCourse(nonExistingAbilityCourseId, inputAbilityCourseDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Ability-Course association does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testUpdateAbilityCourse_Duplicate() throws DuplicateException {
        long abilityCourseId = 1L;
        AbilityCourseDto inputAbilityCourseDto = new AbilityCourseDto();
        when(iAbilityCourseService.updateAbilityCourse(abilityCourseId, inputAbilityCourseDto))
                .thenThrow(new DuplicateException("Ability-Course association already exists!"));
        ResponseEntity<ApiResponse<AbilityCourseDto>> responseEntity = abilityCourseController.updateAbilityCourse(abilityCourseId, inputAbilityCourseDto);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Ability-Course association already exists!", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testDeleteAbilityCourseAssociation_Success() {
        Ability ability = new Ability(1L, "Ability1", null, null,false);
        Course course = new Course(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");

        AbilityCourse abilityCourseToDelete = new AbilityCourse(1L, course, ability,false);
        ResponseEntity<ApiResponse<String>> responseEntity = abilityCourseController.deleteAbilityCourse(abilityCourseToDelete.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Deleted relationship between ability and course chose", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        verify(iAbilityCourseService).deleteAbilityCourse(abilityCourseToDelete.getId());
    }

    @Test
    public void testDeleteAbilityCourse_ResourceNotFound() {
        long nonExistingAbilityCourseId = 999L;
        doThrow(new ResourceNotFoundException("The ability course association does not exist")).when(iAbilityCourseService).deleteAbilityCourse(nonExistingAbilityCourseId);
        ResponseEntity<ApiResponse<String>> responseEntity = abilityCourseController.deleteAbilityCourse(nonExistingAbilityCourseId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The ability course association does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    private  List<AbilityCourse> getAbilityCourseListData() {
        Ability ability1 = new Ability(1L, "Ability1", null, null,false);
        Course course1 = new Course(
                1L, "randomNameOfCourse", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        Ability ability2 = new Ability(2L, "Ability2", null, null,false);
        Course course2 = new Course(
                2L, "randomNameOfCourse2", "randomImage", "randmDescription", null, null,
                null,false,null,null,null,"randomImage");
        AbilityCourse abilityCourse1 = new AbilityCourse(1L, course1, ability1,false);
        AbilityCourse abilityCourse2 =  new AbilityCourse(2L, course2, ability2,false);
        List<AbilityCourse> abilityCourseList = java.util.Arrays.asList(abilityCourse1,abilityCourse2);
        return abilityCourseList;
    }



}
