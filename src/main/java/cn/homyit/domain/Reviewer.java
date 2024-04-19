package cn.homyit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_usercampus")
public class Reviewer {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String real_name;
    private String site;

    private String workplace;
    private String type;
}
