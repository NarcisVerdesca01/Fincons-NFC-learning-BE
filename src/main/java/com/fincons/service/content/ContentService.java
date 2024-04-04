package com.fincons.service.content;

import com.fincons.dto.ContentDto;
import com.fincons.entity.Content;
import com.fincons.entity.Lesson;
import com.fincons.exception.ResourceNotFoundException;
import com.fincons.repository.ContentRepository;
import com.fincons.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ContentService implements IContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public Content findById(long id) {

        if (!contentRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The content does not exist!");
        }
        return contentRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public List<Content> findAllContent() {
        return contentRepository.findAllByDeletedFalse();
    }

    @Override
    public List<Content> findAllNotAssociatedContentWithLesson() {
        return contentRepository.findAllByDeletedFalseAndLessonIsNull();
    }

    @Override
    public Content createContent(ContentDto contentDto) {
        try{
            Content newContent= new Content();

            defineContentType(contentDto,newContent);

            Content savedContent= contentRepository.save(newContent);
            return savedContent;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteContent(long id) {
        if (!contentRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("The content does not exist");
        }

        Content contentToDelete = contentRepository.findByIdAndDeletedFalse(id);
        contentToDelete.setDeleted(true);
        contentRepository.save(contentToDelete);

        Lesson lesson = lessonRepository.findByContent(contentToDelete);
        if (lesson != null) {
            lesson.setContent(null);
            lessonRepository.save(lesson);
        }
    }

    @Override
    public Content updateContent(long id, ContentDto contentDto) {
        Content contentToModify = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content does not exist."));

        if (contentDto.getContent() == null && contentDto.getTypeContent()==null) {
            throw new IllegalArgumentException("Content is null");
        }

        defineContentType(contentDto,contentToModify);
        return contentRepository.save(contentToModify);
    }

    private void defineContentType(ContentDto contentDto, Content newContent){
        String content = contentDto.getContent();
        if(content.contains("youtube")){
            String embedLink= buildYoutubeLink(content);
            newContent.setContent(embedLink);
            newContent.setTypeContent("video");
        } else if(content.contains(".pdf")){
            newContent.setTypeContent("pdf");
            newContent.setContent(content);
        } else if(content.contains(".txt")){
            newContent.setTypeContent("txt");
            newContent.setContent(content);
        }
    }
    private String buildYoutubeLink(String url){
        String videoId = null;
        String regex = "v=([a-zA-Z0-9_-]{11})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            videoId = matcher.group(1);
        }

        return "https://www.youtube.com/embed/"+videoId;
    }


}
