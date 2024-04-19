package cn.homyit.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@TableName("club")
@TableName("t_club")
public class Club implements Serializable {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String name;


    @TableField("introduction")
    private String profile;

    private String president;

    @TableField("picture")
    private String logo;

    @TableField("time")
    private Date createTime;
    //社团创立时间


    public Club(String name, String profile, Date createTime,String president,String logo) {
        this.name = name;
        this.profile = profile;
        this.createTime = createTime;

        this.logo=logo;
        this.president=president;
    }
}