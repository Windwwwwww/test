package cn.homyit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/17 14:16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private String tags;//标签
    private String author;
    private Integer authorId;//作者id
    private String images;//所有的images
    private String authorImage;//作者头像
    private String commentIds;//评论id数组
    private Integer likes;//点赞数
    @TableField(exist = false)
    private Integer likesParam;//是否点赞的判断标识,0为已点赞,1为未点赞
    private LocalDateTime publicationTime;
    private LocalDateTime updateTime;
}
