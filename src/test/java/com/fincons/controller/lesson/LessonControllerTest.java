package com.fincons.controller.lesson;

import com.fincons.controller.LessonController;
import com.fincons.dto.LessonDto;
import com.fincons.entity.Content;
import com.fincons.entity.Lesson;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.jwt.JwtTokenProvider;
import com.fincons.mapper.LessonMapper;
import com.fincons.service.lesson.ILessonService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class LessonControllerTest {


    @Autowired
    private LessonController lessonController;

    @MockBean
    private ILessonService iLessonService;

    @Autowired
    private LessonMapper lessonMapper;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;


    @Test
    public void testGetAllLessons_Success() {
        Lesson lesson = new Lesson(1L, "randomTitle", null, null, null, "randomSecondImage",false, null, null, null, null);
        Lesson lesson2 = new Lesson(1L, "randomTitle2", null, null, null, "randomSecondImage",false, null, null, null, null);
        List<Lesson> lessonList = Arrays.asList(lesson, lesson2);
        when(iLessonService.findAllLessons()).thenReturn(lessonList);
        List<LessonDto> lessonDtoList = lessonList.stream().map(l -> lessonMapper.mapLessonToLessonDto(l)).toList();
        ResponseEntity<ApiResponse<List<LessonDto>>> responseEntity = lessonController.getAllLessons();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<LessonDto> responseCourses = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(responseCourses);
        assertEquals(2, responseCourses.size());
        assertEquals("randomTitle", responseCourses.get(0).getTitle());
        assertEquals("randomTitle2", responseCourses.get(1).getTitle());
    }

    @Test
    public void testGetLessonById_Success(){
        Lesson lesson = new Lesson(1L, "randomTitle", null, null, null, "randomSecondImage",false, null, null, null, null);
        when(iLessonService.findLessonById(1L)).thenReturn(lesson);
        ResponseEntity<ApiResponse<LessonDto>> responseEntity = lessonController.getLessonById(lessonMapper.mapLessonToLessonDto(lesson).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        LessonDto lessonDto = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(lessonDto);
        assertEquals(1, lessonDto.getId());
        assertEquals(lesson.getId(), lessonDto.getId());
        assertEquals(lesson.getTitle(), lessonDto.getTitle());
    }

    @Test
    public void testGetLessonById_ResourceNotFound(){
        long nonExistingLessontId = 1L;
        doThrow(new ResourceNotFoundException("The lesson does not exist")).when(iLessonService).findLessonById(nonExistingLessontId);
        ResponseEntity<ApiResponse<LessonDto>> responseEntity = lessonController.getLessonById(nonExistingLessontId);
        assertEquals("The lesson does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }

    @Test
    public void testCreateLesson_Success() throws DuplicateException {
        LessonDto inputLessonDto = new LessonDto(1L, "randomTitle", null, null, null, "randomSecondImage",false, null, null, null, null);
        when(iLessonService.createLesson(inputLessonDto)).thenReturn(lessonMapper.mapDtoToLessonEntity(inputLessonDto));
        ResponseEntity<ApiResponse<LessonDto>> responseEntity = lessonController.createLesson(inputLessonDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputLessonDto.getTitle(), Objects.requireNonNull(responseEntity.getBody()).getData().getTitle());
        verify(iLessonService).createLesson(inputLessonDto);
    }

    @Test
    public void testCreateLesson_InvalidInput() throws DuplicateException {
        LessonDto invalidLessonDto = new LessonDto();
        when(iLessonService.createLesson(invalidLessonDto)).thenThrow(new IllegalArgumentException("Title not present"));
        ResponseEntity<ApiResponse<LessonDto>> responseEntity = lessonController.createLesson(invalidLessonDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Title not present", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }



    @Test
    public void testUpdateLesson_Success() throws DuplicateException {
        long lessonId = 1L;
        LessonDto inputLessonDto = new LessonDto(1L, "randomTitle", null, null, null, "randomSecondImage",false, null, null, null, null);
        Lesson updatedLesson = new Lesson(1L, "randomTitle", null, null, null, "randomSecondImage",false, null, null, null, null);
        when(iLessonService.updateLesson(lessonId, inputLessonDto)).thenReturn(updatedLesson);
        ResponseEntity<ApiResponse<LessonDto>> responseEntity = lessonController.updateLesson(lessonId, inputLessonDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedLesson.getTitle(), Objects.requireNonNull(responseEntity.getBody()).getData().getTitle());
    }

    @Test
    public void testUpdateLesson_ResourceNotFound() throws DuplicateException {
        long nonExistingLessonId = 999L;
        LessonDto inputLessonDto = new LessonDto();
        when(iLessonService.updateLesson(nonExistingLessonId, inputLessonDto))
                .thenThrow(new ResourceNotFoundException("Lesson does not exist"));
        ResponseEntity<ApiResponse<LessonDto>> responseEntity = lessonController.updateLesson(nonExistingLessonId, inputLessonDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Lesson does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testDeleteLesson_Success() {
        long lessonId = 1L;
        ResponseEntity<ApiResponse<String>> responseEntity = lessonController.deleteLesson(lessonId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The lesson has been successfully deleted!", Objects.requireNonNull(responseEntity.getBody()).getData());
        verify(iLessonService).deleteLesson(lessonId);
    }

    @Test
    public void testDeleteAbility_ResourceNotFound() {
        long nonExistingLessonId = 999L;
        doThrow(new ResourceNotFoundException("The lesson does not exist")).when(iLessonService).deleteLesson(nonExistingLessonId);
        ResponseEntity<ApiResponse<String>> responseEntity = lessonController.deleteLesson(nonExistingLessonId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The lesson does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testAssociateContentToLesson_Success() throws DuplicateException {
        Lesson lessonToAssociate = new Lesson(1L, "randomTitle", null, null, null, "randomSecondImage",false, null, null, null, null);
        Content contentToAssociate = new Content(1L, "video", "randomVideo",null,false);
        when(iLessonService.associateContentToLesson(1L,1L)).thenReturn(lessonToAssociate);
        Lesson result = iLessonService.associateContentToLesson(1L, 1L);
        assertEquals(lessonToAssociate, result);
    }


}
