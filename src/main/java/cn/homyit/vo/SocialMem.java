package cn.homyit.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 社团成员
 */
@Data
@TableName("t_social_member")
public class SocialMem {
    @TableId(type = IdType.AUTO)
    private Integer id;

    //社员名字
    private String name;

    private Integer memberId;

    //社团名字
    private String socialName;
    //头像
    private String picture;

    /**
     * 加入社团时间
     */
    @TableField(value = "time")
    private String time;
}

