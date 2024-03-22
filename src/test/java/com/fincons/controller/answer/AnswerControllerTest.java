package com.fincons.controller.answer;

import com.fincons.controller.AnswerController;
import com.fincons.dto.AnswerDto;
import com.fincons.entity.Answer;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.AnswerMapper;
import com.fincons.service.answer.IAnswerService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class AnswerControllerTest {


    @Autowired
    private AnswerController answerController;
    @MockBean
    private IAnswerService iAnswerService;
    @Autowired
    private AnswerMapper answerMapper;


    @Test
    void testGetAllAnswers_Success(){
        List<Answer> answersList = Arrays.asList(new Answer(1L,"JEE: Java Enterprise Edition",null,true,false),
                new Answer(1L,"JEE: Java Enterprise Edition",null,true,true),
                new Answer(2L,"JEE: Java Enterprise Enter",null,true,false));
        when(iAnswerService.findAllAnswer()).thenReturn(answersList
                .stream()
                .filter(a->!a.isDeleted())
                .toList());
        ResponseEntity<ApiResponse<List<AnswerDto>>> responseEntity = answerController.getAllAnswer();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<AnswerDto> responseAnswers = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(responseAnswers);
        assertEquals(2, responseAnswers.size());
        assertEquals("JEE: Java Enterprise Edition", responseAnswers.get(0).getText());
        assertEquals("JEE: Java Enterprise Enter", responseAnswers.get(1).getText());
        assertFalse( responseAnswers.get(0).isDeleted());
        assertFalse(responseAnswers.get(1).isDeleted());
    }

    @Test
    void testGetAnswerById_Success() throws DuplicateException {
        Answer answer = new Answer(1L, "JEE: Java Enterprise Edition",null,true,false);
        when(iAnswerService.findById(1L)).thenReturn(answer);
        ResponseEntity<ApiResponse<AnswerDto>> responseEntity = answerController.getById(answerMapper.mapAnswerToAnswerDto(answer).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).getData());
        assertEquals(answer.getId(), responseEntity.getBody().getData().getId());
        assertEquals(answer.getText(), responseEntity.getBody().getData().getText());
        assertFalse(responseEntity.getBody().getData().isDeleted());
    }

    @Test
    void testGetAnswerById_ResourceNotFoundSuccess(){
        long nonExistingAnswerId = 999L;
        doThrow(new ResourceNotFoundException("The answer does not exist")).when(iAnswerService).findById(nonExistingAnswerId);
        ResponseEntity<ApiResponse<AnswerDto>> responseEntity = answerController.getById(nonExistingAnswerId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The answer does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testCreateAnswer_Success() throws DuplicateException {
        AnswerDto inputAnswerDto = new AnswerDto(1L,"JEE: Java Enterprise Edition",null,true,false);
        when(iAnswerService.createAnswer(inputAnswerDto)).thenReturn(answerMapper.mapAnswerDtoToAnswerEntity(inputAnswerDto));
        ResponseEntity<ApiResponse<AnswerDto>> responseEntity = answerController.createAnswer(inputAnswerDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputAnswerDto.getText(), Objects.requireNonNull(responseEntity.getBody()).getData().getText());
        assertFalse(responseEntity.getBody().getData().isDeleted());
    }

    @Test
    void testCreateAnswer_BadRequest() throws DuplicateException {
        AnswerDto inputAnswerDto = new AnswerDto(1L,null,null,true,false);
        when(iAnswerService.createAnswer(inputAnswerDto)).thenThrow(new IllegalArgumentException("User must enter the text of answer!"));
        ResponseEntity<ApiResponse<AnswerDto>> responseEntity = answerController.createAnswer(inputAnswerDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User must enter the text of answer!", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testCreateAnswer_Duplicate() throws DuplicateException {
        AnswerDto inputAnswerDto = new AnswerDto(1L,"JEE: Java Enterprise Edition",null,true,false);
        when(iAnswerService.createAnswer(inputAnswerDto))
                .thenThrow(new DuplicateException("Answer already exists!"));
        ResponseEntity<ApiResponse<AnswerDto>> responseEntity = answerController.createAnswer(inputAnswerDto);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Answer already exists!", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testUpdateAnswer_Success() throws DuplicateException {
        long answerId = 1L;
        AnswerDto inputAnswerDto = new AnswerDto(1L,"JEE: Java Enterprise Edition",null,true,false);
        Answer updatedAnswer = new Answer(1L,"JEE: Java Enterprise Edition",null,true,false);
        when(iAnswerService.updateAnswer(answerId, inputAnswerDto)).thenReturn(updatedAnswer);
        ResponseEntity<ApiResponse<String>> responseEntity = answerController.updateAnswer(answerId, inputAnswerDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The answer has been successfully updated!", Objects.requireNonNull(responseEntity.getBody()).getData());
    }

    @Test
    void testUpdateAnswer_ResourceNotFound() throws DuplicateException {
        long nonExistingAnswerId = 999L;
        AnswerDto inputAnswerDto = new AnswerDto();
        when(iAnswerService.updateAnswer(nonExistingAnswerId, inputAnswerDto))
                .thenThrow(new ResourceNotFoundException("Ability does not exist."));
        ResponseEntity<ApiResponse<String>> responseEntity = answerController.updateAnswer(nonExistingAnswerId, inputAnswerDto);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Ability does not exist.", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testUpdateAnswer_IllegalArgumentException() throws DuplicateException {
        long nonExistingAnswerId = 999L;
        AnswerDto inputAnswerDto = new AnswerDto();
        when(iAnswerService.updateAnswer(nonExistingAnswerId, inputAnswerDto))
                .thenThrow(new IllegalArgumentException("Text of answer is null"));
        ResponseEntity<ApiResponse<String>> responseEntity = answerController.updateAnswer(nonExistingAnswerId, inputAnswerDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Text of answer is null", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testDeleteAnswer_Success() {
        long answerId = 1L;
        ResponseEntity<ApiResponse<String>> responseEntity = answerController.deleteAnswer(answerId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The answer has been successfully deleted!", Objects.requireNonNull(responseEntity.getBody()).getData());
        verify(iAnswerService).deleteAnswer(answerId);
    }

    @Test
    void testDeleteAnswer_ResourceNotFound() {
        long nonExistingAnswerId = 999L;
        doThrow(new ResourceNotFoundException("The answer does not exist")).when(iAnswerService).deleteAnswer(nonExistingAnswerId);
        ResponseEntity<ApiResponse<String>> responseEntity = answerController.deleteAnswer(nonExistingAnswerId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The answer does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testAssociateQuestionToAnswer_Success() {
        long idAnswer = 1L;
        long idQuestion = 2L;

        ResponseEntity<ApiResponse<String>> responseEntity = answerController.associateQuestionToAnswer(idAnswer, idQuestion);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The question has been successfully associated with answer!", Objects.requireNonNull(responseEntity.getBody()).getData());
    }

    @Test
    void testAssociateQuestionToAnswer_ResourceNotFound() throws ResourceNotFoundException, DuplicateException {
        long idAnswer = 1L;
        long idQuestion = 2L;

        doThrow(new ResourceNotFoundException("The answer does not exist")).when(iAnswerService).associateQuestionToAnswer(idAnswer, idQuestion);

        ResponseEntity<ApiResponse<String>> responseEntity = answerController.associateQuestionToAnswer(idAnswer, idQuestion);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The answer does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testAssociateQuestionToAnswer_Duplicate() throws ResourceNotFoundException, DuplicateException {
        long idAnswer = 1L;
        long idQuestion = 2L;

        doThrow(new DuplicateException("Association already exists")).when(iAnswerService).associateQuestionToAnswer(idAnswer, idQuestion);

        ResponseEntity<ApiResponse<String>> responseEntity = answerController.associateQuestionToAnswer(idAnswer, idQuestion);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("Association already exists", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }











}
