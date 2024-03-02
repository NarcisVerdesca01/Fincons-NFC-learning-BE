package com.fincons.controller;

import com.fincons.dto.ContentDto;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.mapper.ContentMapper;
import com.fincons.service.content.IContentService;
import com.fincons.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("${application.context}")
public class ContentController {

    @Autowired
    private IContentService iContentService;
    @Autowired
    private ContentMapper contentMapper;

    @GetMapping("${content.get-all-content}")
    public ResponseEntity<ApiResponse<List<ContentDto>>> getAllLessons(){
        List<ContentDto> contentDtoList= iContentService.findAllContent()
                .stream()
                .map(s->contentMapper.mapContentToContentDto(s))
                .toList();
        return ResponseEntity.ok().body(ApiResponse.<List<ContentDto>>builder()
                .data(contentDtoList)
                .build());
    }
    @GetMapping("${content.get-by-id}/{id}")
    public ResponseEntity<ApiResponse<ContentDto>> getById(@PathVariable long id){
        try{
            ContentDto contentDto= contentMapper.mapContentToContentDto(iContentService.findById(id));
            return ResponseEntity.ok().body(ApiResponse.<ContentDto>builder()
                    .data(contentDto)
                    .build());
        }catch(ResourceNotFoundException resourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<ContentDto>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
    @PostMapping("${content.create}")
    public ResponseEntity<ApiResponse<ContentDto>> createContent(@RequestBody ContentDto contentDto) {
        try {
            ContentDto contentDtoToShow = contentMapper.mapContentToContentDto(iContentService.createContent(contentDto));
            return ResponseEntity.ok().body(ApiResponse.<ContentDto>builder()
                    .data(contentDtoToShow)
                    .build());
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(ApiResponse.<ContentDto>builder()
                    .message(exception.getMessage())
                    .build());
        }
    }

    @PutMapping("${content.update}/{id}")
    public ResponseEntity<ApiResponse<String>> updateContent(@PathVariable long id,@RequestBody ContentDto contentDto) {
        try {
            iContentService.updateContent(id,contentDto);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The content has been successfully updated!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }
    @DeleteMapping("${content.delete}/{id}")
    public ResponseEntity<ApiResponse<String>> deleteContent(@PathVariable long id) {
        try {
            iContentService.deleteContent(id);
            return ResponseEntity.ok().body(ApiResponse.<String>builder()
                    .data("The content has been successfully deleted!")
                    .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<String>builder()
                    .message(resourceNotFoundException.getMessage())
                    .build());
        }
    }




}
