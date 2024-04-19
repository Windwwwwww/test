package cn.homyit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@TableName("club_members")
@TableName("t_social_member")
public class ClubMembers implements Serializable {
    @TableId(type= IdType.AUTO)
    private Integer id;

    @TableField("social_name")
    private String club_name;
    private String name;

    private String picture;

    @TableField("time")
    private Date attendTime;

    public ClubMembers(String name,  Date attendTime,String club_name) {

        this.name = name;
        this.attendTime = attendTime;
        this.club_name=club_name;
    }
}
