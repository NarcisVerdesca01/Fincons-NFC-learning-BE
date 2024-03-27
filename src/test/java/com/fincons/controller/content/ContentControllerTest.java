package com.fincons.controller.content;

import com.fincons.controller.ContentController;
import com.fincons.dto.ContentDto;
import com.fincons.entity.Content;
import com.fincons.exception.DuplicateException;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.ContentMapper;
import com.fincons.service.content.IContentService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ContentControllerTest {

    @Autowired
    private ContentController contentController;
    @MockBean
    private IContentService iContentService;
    @Autowired
    private ContentMapper contentMapper;


    @Test
    void testGetAllContents_Success() {
        List<Content> contents = Arrays.asList(new Content(1L, "video", "randomContent", null,false),
                new Content(3L, "video", "randomContent2", null,true),
        new Content(2L, "video", "randomContent2", null,false));
        when(iContentService.findAllContent()).thenReturn(contents
                .stream()
                        .filter(c->!c.isDeleted())
                .toList());
        ResponseEntity<ApiResponse<List<ContentDto>>> responseEntity = contentController.getAllContent();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<ContentDto> responseContents = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(responseContents);
        assertEquals(2, responseContents.size());
        assertEquals("randomContent", responseContents.get(0).getContent());
        assertEquals("randomContent2", responseContents.get(1).getContent());
        assertFalse( responseContents.get(0).isDeleted());
        assertFalse(responseContents.get(1).isDeleted());
    }

    @Test
    void testGetContentById_Success(){
        Content content = new Content(1L,"video","randomContent",null,false);
        when(iContentService.findById(1L)).thenReturn(content);
        ResponseEntity<ApiResponse<ContentDto>> responseEntity = contentController.getById(contentMapper.mapContentToContentDto(content).getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ContentDto contentDto = Objects.requireNonNull(responseEntity.getBody()).getData();
        assertNotNull(contentDto);
        assertEquals(1, contentDto.getId());
        assertEquals(content.getId(), contentDto.getId());
        assertEquals(content.getContent(), contentDto.getContent());
        assertFalse(responseEntity.getBody().getData().isDeleted());
    }

    @Test
    void testGetContentById_ResourceNotFound() {
        long nonExistingContentId = 999L;
        doThrow(new ResourceNotFoundException("The content does not exist")).when(iContentService).findById(nonExistingContentId);
        ResponseEntity<ApiResponse<ContentDto>> responseEntity = contentController.getById(nonExistingContentId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("The content does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testCreateContent_Success() throws DuplicateException {
        ContentDto inputContentDto = new ContentDto(1L,"video","randomContent",null,false);
        when(iContentService.createContent(inputContentDto)).thenReturn(contentMapper.mapContentDtoToContentEntity(inputContentDto));
        ResponseEntity<ApiResponse<ContentDto>> responseEntity = contentController.createContent(inputContentDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(inputContentDto.getContent(), Objects.requireNonNull(responseEntity.getBody()).getData().getContent());
        assertFalse(responseEntity.getBody().getData().isDeleted());
    }

    @Test
    void testCreateContent_RuntimeException() throws DuplicateException {
        when(iContentService.createContent(any(ContentDto.class)))
                .thenThrow(new RuntimeException("Something went wrong"));
        ResponseEntity<ApiResponse<ContentDto>> responseEntity = contentController.createContent(new ContentDto());
        assert responseEntity != null;
        assert responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert responseEntity.getBody() != null;
        assert responseEntity.getBody().getMessage().equals("Something went wrong");
    }

    @Test
    void testUpdateContent_Success() throws DuplicateException {
        long contentId = 1L;
        ContentDto inputContentDto = new ContentDto(1L,"video","randomContent",null,false);
        Content updatedContent = new Content(1L,"video","randomContent",null,false);
        when(iContentService.updateContent(contentId, inputContentDto)).thenReturn(updatedContent);
        ResponseEntity<ApiResponse<String>> responseEntity = contentController.updateContent(contentId, inputContentDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("The content has been successfully updated!", Objects.requireNonNull(responseEntity.getBody()).getData());
    }

    @Test
    void testUpdateContent_ResourceNotFoundException(){
        long nonExistingContentId = 999L;
        ContentDto inputContentDto = new ContentDto();
        when(iContentService.updateContent(nonExistingContentId,inputContentDto))
                .thenThrow(new ResourceNotFoundException("Content does not exist."));
        ResponseEntity<ApiResponse<String>> responseEntity = contentController.updateContent(nonExistingContentId,inputContentDto);
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
        assertEquals("Content does not exist.", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testUpdateContent_IllegalArgumentException() {
        long nonExistingContentId = 999L;
        ContentDto inputContentDto = new ContentDto();
        when(iContentService.updateContent(nonExistingContentId,inputContentDto))
                .thenThrow(new IllegalArgumentException("Content is null."));
        ResponseEntity<ApiResponse<String>> responseEntity = contentController.updateContent(nonExistingContentId,inputContentDto);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        assertEquals("Content is null.", Objects.requireNonNull(responseEntity.getBody()).getMessage());
    }

    @Test
    void testDeleteAnswer_Success() {
      long contentId = 1L;
    ResponseEntity<ApiResponse<String>> responseEntity = contentController.deleteContent(contentId);
    assertEquals("The content has been successfully deleted!", Objects.requireNonNull(responseEntity.getBody()).getData());
    assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
    verify(iContentService).deleteContent(contentId);
    }

    @Test
    void testDeleteAnswer_ResourceNotFoundException() {
        long nonExistingContentId = 1L;
        doThrow(new ResourceNotFoundException("The content does not exist")).when(iContentService).deleteContent(nonExistingContentId);
        ResponseEntity<ApiResponse<String>> responseEntity = contentController.deleteContent(nonExistingContentId);
        assertEquals("The content does not exist", Objects.requireNonNull(responseEntity.getBody()).getMessage());
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
    }





}
