package cn.homyit.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 场地
 */
@Data
@TableName("t_join_site")
public class JoinSite {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String academy;
    private String reviewer;
    private String time;
    private String period;
    private String date;
    private String purpose;
    private String applicant;
}
