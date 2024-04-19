package cn.homyit.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@TableName("t_join_society")
public class Social {
    @TableId(type = IdType.AUTO)
    private Integer id;


    //成员数量
    private Long count;
    //社团名字
    private String name;
    //社团简介
    private String introduction;
    //社团社长
    private String president;
    //审核员
    private String reviewer;
    //社团图标
    private String picture;

    private Integer status;


    @TableField(value = "time")
    private String time;
}
