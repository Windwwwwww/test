package cn.homyit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("t_site")

public class Site implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    //场地类型
    private String site;

    //场地审核方式：一级，二级
    private Integer type;

    private String roomNum;
}

