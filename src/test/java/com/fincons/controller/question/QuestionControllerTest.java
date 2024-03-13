package com.fincons.controller.question;

import com.fincons.controller.QuestionController;
import com.fincons.dto.QuestionDto;
import com.fincons.entity.Question;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.QuestionMapper;
import com.fincons.service.question.IQuestionService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class QuestionControllerTest {

    @Autowired
    private QuestionController questionController;
    @MockBean
    private IQuestionService iQuestionService;
    @Autowired
    private QuestionMapper questionMapper;

    @Test
    public void testGetAllQuestion_Success() {
        // Mock data
        List<Question> questions = Arrays.asList(
                new Question(1L, "Question1", null, null,3,false),
                new Question(2L, "Question2", null, null,3,false)
        );
        when(iQuestionService.findAllQuestion()).thenReturn(questions);
        List<QuestionDto> questionDtos = Arrays.asList(
                new QuestionDto(1L, "Question1", null, null,3,false),
                new QuestionDto(2L, "Question2", null, null,3,false)
        );
        ResponseEntity<ApiResponse<List<QuestionDto>>> responseEntity = questionController.getAllQuestion();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<QuestionDto> responseQuestions = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(responseQuestions);
        assertEquals(2, responseQuestions.size());
        assertEquals("Question1", responseQuestions.get(0).getTextQuestion());
        assertEquals("Question2", responseQuestions.get(1).getTextQuestion());
    }

    @Test
    public void testGetQuestionById_Success() {
        Question question = new Question(1L, "What is Spring Boot?", null, null,3,false);
        when(iQuestionService.findById(1L)).thenReturn(question);
        ResponseEntity<ApiResponse<QuestionDto>> responseEntity = questionController.getById(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getData());
        assertEquals(question.getId(), responseEntity.getBody().getData().getId());
        assertEquals(question.getTextQuestion(), responseEntity.getBody().getData().getTextQuestion());
    }


    @Test
    public void testGetQuestionById_ResourceNotFound() {
        when(iQuestionService.findById(1L)).thenThrow(new ResourceNotFoundException("Question not found"));
        ResponseEntity<ApiResponse<QuestionDto>> responseEntity = questionController.getById(1L);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Question not found", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    public void testCreateQuestion_Success() {
        QuestionDto inputQuestionDto = new QuestionDto(1L, "What is Spring Boot?", null, null,3,false);
        when(iQuestionService.createQuestion(inputQuestionDto)).thenReturn(questionMapper.mapQuestionDtoToQuestionEntity(inputQuestionDto));
        ResponseEntity<ApiResponse<QuestionDto>> responseEntity = questionController.createQuestion(inputQuestionDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputQuestionDto.getTextQuestion(), Objects.requireNonNull(responseEntity.getBody()).getData().getTextQuestion());
    }


    @Test
    public void testUpdateQuestion_Success() {

        QuestionDto questionDto = new QuestionDto();
        Question updatedQuestion = new Question();
        when(iQuestionService.updateQuestion(1L, questionDto)).thenReturn(updatedQuestion);
        ResponseEntity<ApiResponse<String>> responseEntity = questionController.updateQuestion(1L, questionDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The question has been successfully updated!", Objects.requireNonNull(responseEntity.getBody()).getData());
    }

    @Test
    public void testUpdateQuestion_ResourceNotFound() {
        QuestionDto questionDto = new QuestionDto();
        when(iQuestionService.updateQuestion(1L, questionDto)).thenThrow(new ResourceNotFoundException("Question not found"));
        ResponseEntity<ApiResponse<String>> responseEntity = questionController.updateQuestion(1L, questionDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Question not found", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }
    @Test
    public void testDeleteQuestion_Success() {
        ResponseEntity<ApiResponse<String>> responseEntity = questionController.deleteQuestion(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The question has been successfully deleted!", responseEntity.getBody().getData());
        verify(iQuestionService).deleteQuestion(1L);
    }


    @Test
    public void testDeleteQuestion_ResourceNotFound() {
        doThrow(new ResourceNotFoundException("The question does not exist")).when(iQuestionService).deleteQuestion(1L);
        ResponseEntity<ApiResponse<String>> responseEntity = questionController.deleteQuestion(1L);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The question does not exist", responseEntity.getBody().getMessage());
    }

}
