package com.fincons.controller.quiz;

import com.fincons.controller.QuizController;
import com.fincons.dto.QuizDto;
import com.fincons.entity.Quiz;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.QuizMapper;
import com.fincons.service.quiz.IQuizService;
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
public class QuizControllerTest {


    @Autowired
    private QuizController quizController;
    @MockBean
    private IQuizService iQuizService;
    @Autowired
    private QuizMapper quizMapper;

    @Test
    public void testGetAllQuizzes_Success() {
        List<Quiz> quizzes = Arrays.asList(new Quiz(1L, "quizTitle", null, null, null, null, null, null, null),
                new Quiz(2L, "quizTitle2", null, null, null, null, null, null, null));
        when(iQuizService.findAllQuiz()).thenReturn(quizzes);
        List<QuizDto> quizDtos = Arrays.asList(new QuizDto(1L, "quizTitle", null, null, null, null, null, null, null),
                new QuizDto(2L, "quizTitle2", null, null, null, null, null, null, null));
        ResponseEntity<ApiResponse<List<QuizDto>>> responseEntity = quizController.getAllQuiz();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<QuizDto> responseQuiz = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(responseQuiz);
        assertEquals(2, responseQuiz.size());
        assertEquals("quizTitle", responseQuiz.get(0).getTitle());
        assertEquals("quizTitle2", responseQuiz.get(1).getTitle());
    }


    @Test
    public void testGetQuizById_Success() throws DuplicateException {
        Quiz quiz = new Quiz(1L, "quizTitle", null, null, null, null, null, null, null);
        when(iQuizService.findById(1L)).thenReturn(quiz);
        ResponseEntity<ApiResponse<QuizDto>> responseEntity = quizController.getById(quizMapper.mapQuizToQuizDto(quiz).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getData());
        assertEquals(quiz.getId(), responseEntity.getBody().getData().getId());
        assertEquals(quiz.getTitle(), responseEntity.getBody().getData().getTitle());
    }


    @Test
    public void testGetQuizById_ResourceNotFound() {
        when(iQuizService.findById(1L)).thenThrow(new ResourceNotFoundException("Quiz not found"));
        ResponseEntity<ApiResponse<QuizDto>> responseEntity = quizController.getById(1L);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Quiz not found", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testCreateQuiz_Success() throws DuplicateException {
        QuizDto inputQuizDto = new QuizDto(1L, "quizTitle", null, null, null, null, null, null, null);
        when(iQuizService.createQuiz(inputQuizDto)).thenReturn(quizMapper.mapQuizDtoToQuizEntity(inputQuizDto));
        ResponseEntity<ApiResponse<QuizDto>> responseEntity = quizController.createQuiz(inputQuizDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputQuizDto.getTitle(), Objects.requireNonNull(responseEntity.getBody()).getData().getTitle());
    }

    @Test
    public void testCreateQuiz_Duplicate() throws DuplicateException {
        QuizDto inputQuizDto = new QuizDto(1L, "quizTitle", null, null, null, null, null, null, null);
        when(iQuizService.createQuiz(inputQuizDto)).thenThrow(new DuplicateException("The name of quiz already exist"));
        ResponseEntity<ApiResponse<QuizDto>> responseEntity = quizController.createQuiz(inputQuizDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("The name of quiz already exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }


    @Test
    public void testUpdateQuiz_Success() throws DuplicateException {
        long quizId = 1L;
        QuizDto inputQuizDto = new QuizDto(1L, "quizTitle", null, null, null, null, null, null, null);
        Quiz updatedQuiz = new Quiz(1L, "quizTitle", null, null, null, null, null, null, null);
        when(iQuizService.updateQuiz(quizId, inputQuizDto)).thenReturn(updatedQuiz);
        ResponseEntity<ApiResponse<String>> responseEntity = quizController.updateQuiz(quizId, inputQuizDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The quiz has been successfully updated!", Objects.requireNonNull(Objects.requireNonNull(responseEntity.getBody()).getData()));
    }

    @Test
    public void testUpdateQuiz_ResourceNotFound() throws DuplicateException {
        long nonExistingQuizId = 999L;
        QuizDto inputQuizDto = new QuizDto();
        when(iQuizService.updateQuiz(nonExistingQuizId, inputQuizDto))
                .thenThrow(new ResourceNotFoundException("Quiz does not exist"));
        ResponseEntity<ApiResponse<String>> responseEntity = quizController.updateQuiz(nonExistingQuizId, inputQuizDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Quiz does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testDeleteQuiz_Success() {
        long quizId = 1L;
        ResponseEntity<ApiResponse<String>> responseEntity = quizController.deleteQuiz(quizId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The quiz has been successfully deleted!", Objects.requireNonNull(responseEntity.getBody()).getData());
        verify(iQuizService).deleteQuiz(quizId);
    }

    @Test
    public void testDeleteQuiz_ResourceNotFound() {
        long nonExistingQuizId = 999L;
        doThrow(new ResourceNotFoundException("The quiz does not exist")).when(iQuizService).deleteQuiz(nonExistingQuizId);
        ResponseEntity<ApiResponse<String>> responseEntity = quizController.deleteQuiz(nonExistingQuizId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The quiz does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }


}