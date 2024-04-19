package cn.homyit.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/5 13:14
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_userCampus")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String account;
    private String userName;
    private String password;
    private String param;
    private String type;//普通用户为0,超级管理员为3,一级管理员为1，二级管理员为2
    private String realName;
    private String workplace;//工作单位
    private String email;
    private String image;//图片url路径
    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public User(int id,String type){
        this.id=id;
        this.type=type;
    }


}
