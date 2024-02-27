package com.fincons.service.content;

import com.fincons.entity.Content;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IContentService {
     List<Content> findAllContent();
     Content createContent(MultipartFile file);
}
