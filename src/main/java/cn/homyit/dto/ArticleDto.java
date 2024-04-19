package cn.homyit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/20 17:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private MultipartFile[] files;
    private Integer id;
    private String title;
    private String content;
    private String tags;//标签

    public ArticleDto(MultipartFile[] files, String title, String content, String tags) {
        this.files = files;
        this.title = title;
        this.content = content;
        this.tags = tags;
    }
}
