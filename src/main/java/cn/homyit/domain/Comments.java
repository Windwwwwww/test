package cn.homyit.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
public class Comments implements Serializable {

    @TableId
    private Integer id;

    private String isVisible;

    private String nickname;

    private String content;

    private Integer articleId;

    private Integer parentId;

    private Integer sort;

    private String createTime;

    @TableField(exist = false)
    private List<Comments> replyComments = new ArrayList<>();

    @TableField(exist = false)
    private Comments parentComment;

    private String email;


}
