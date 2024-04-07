package com.fincons.controller.lesson;

import com.fincons.controller.LessonController;
import com.fincons.dto.LessonDto;
import com.fincons.entity.Content;
import com.fincons.entity.Lesson;
import com.fincons.entity.Role;
import com.fincons.entity.User;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.jwt.JwtTokenProvider;
import com.fincons.mapper.LessonMapper;
import com.fincons.service.authorization.AuthService;
import com.fincons.service.lesson.ILessonService;
import com.fincons.utility.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
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

    @MockBean
    private AuthService authService;

    @MockBean
    private PasswordEncoder passwordEncoder;



    @Test
    public void testGetAllLessons_Success() {
        Lesson lesson = new Lesson(1L, "randomTitle", null, null, null, "randomSecondImage",false, null, null, null, null);
        Lesson lesson2 = new Lesson(1L, "randomTitle2", null, null, null, "randomSecondImage",false, null, null, null, null);
        Lesson lesson3 = new Lesson(3L, "randomTitle2", null, null, null, "randomSecondImage",true, null, null, null, null);

        List<Lesson> lessonList = Arrays.asList(lesson, lesson2,lesson3);
        when(iLessonService.findAllLessons()).thenReturn(lessonList
                .stream()
                .filter(l->!l.isDeleted())
                .toList());
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
        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("Password!"));
        Role role = new Role(1L,"ROLE_ADMIN",null,false);
        admin.setRoles(List.of(role));
        Authentication auth = new UsernamePasswordAuthenticationToken(admin.getEmail(), admin.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(admin.getEmail());
        LessonDto inputLessonDto = new LessonDto(1L, "randomTitle", null, null, null, "randomSecondImage",false, null, null, null, null);
        when(iLessonService.createLesson(inputLessonDto)).thenReturn(lessonMapper.mapDtoToLessonEntity(inputLessonDto));
        ResponseEntity<ApiResponse<LessonDto>> responseEntity = lessonController.createLesson(inputLessonDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputLessonDto.getTitle(), Objects.requireNonNull(responseEntity.getBody()).getData().getTitle());
        verify(iLessonService).createLesson(inputLessonDto);
    }

    @Test
    public void testCreateLesson_InvalidInput() throws DuplicateException {
        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("Password!"));
        Role role = new Role(1L,"ROLE_ADMIN",null,false);
        admin.setRoles(List.of(role));
        Authentication auth = new UsernamePasswordAuthenticationToken(admin.getEmail(), admin.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(admin.getEmail());
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
        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("Password!"));
        Role role = new Role(1L,"ROLE_ADMIN",null,false);
        admin.setRoles(List.of(role));
        Authentication auth = new UsernamePasswordAuthenticationToken(admin.getEmail(), admin.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(admin.getEmail());
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
        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("Password!"));
        Role role = new Role(1L,"ROLE_ADMIN",null,false);
        admin.setRoles(List.of(role));
        Authentication auth = new UsernamePasswordAuthenticationToken(admin.getEmail(), admin.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(admin.getEmail());
        long lessonId = 1L;
        ResponseEntity<ApiResponse<String>> responseEntity = lessonController.deleteLesson(lessonId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The lesson has been successfully deleted!", Objects.requireNonNull(responseEntity.getBody()).getData());
        verify(iLessonService).deleteLesson(lessonId);
    }

    @Test
    public void testDeleteAbility_ResourceNotFound() {
        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("Password!"));
        Role role = new Role(1L,"ROLE_ADMIN",null,false);
        admin.setRoles(List.of(role));
        Authentication auth = new UsernamePasswordAuthenticationToken(admin.getEmail(), admin.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer mock_token");
        when(jwtTokenProvider.getEmailFromJWT("mock_token")).thenReturn(admin.getEmail());
        long nonExistingLessonId = 999L;
        doThrow(new ResourceNotFoundException("The lesson does not exist")).when(iLessonService).deleteLesson(nonExistingLessonId);
        ResponseEntity<ApiResponse<String>> responseEntity = lessonController.deleteLesson(nonExistingLessonId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The lesson does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testAssociateContentToLesson_Success() throws DuplicateException, SQLIntegrityConstraintViolationException {
        Lesson lessonToAssociate = new Lesson(1L, "randomTitle", null, null, null, "randomSecondImage",false, null, null, null, null);
        Content contentToAssociate = new Content(1L, "video", "randomVideo",null,false);
        when(iLessonService.associateContentToLesson(1L,1L)).thenReturn(lessonToAssociate);
        Lesson result = iLessonService.associateContentToLesson(1L, 1L);
        assertEquals(lessonToAssociate, result);
    }


}
