package com.fincons.service.content;

import com.fincons.entity.Content;
import com.fincons.repository.ContentRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class ContentService implements IContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Override
    public List<Content> findAllContent() {
        return contentRepository.findAll();
    }

    public Content createContent(MultipartFile file){
            try{
                String tipoContent= getTypeOfContent(file);
                Content newContent= new Content();
                newContent.setTypeContent(tipoContent);
                Content savedContent= contentRepository.save(newContent);
                return savedContent;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }

        private String getTypeOfContent(MultipartFile file){
            String extesions= FilenameUtils.getExtension(file.getOriginalFilename());
            return extesions;
        }

}
