package cn.homyit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/18 18:24
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String url;
    private String idParam;//形式"文章：" + 文章id 或”用户：“ + 用户id
}
