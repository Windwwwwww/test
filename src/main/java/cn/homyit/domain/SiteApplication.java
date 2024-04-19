package cn.homyit.domain;

import cn.homyit.domain.Enum.ApplicationState;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 作者名
 * @since 2024-03-26
 */
@Data
@TableName("t_join_site")
//@TableName("site_application")
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SiteApplication implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private ApplicationState status;


    private String name;//eg:惟义楼W2302
//    private int siteId;//地点对应的site数据库id

    @TableField("advice")
    private String appAdvice;
    //审批意见


    private String purpose;
    //申请理由

    @TableField(fill = FieldFill.INSERT)
    private Integer applicantId;
    //申请人id
    @TableField(fill = FieldFill.INSERT)
    private String applicant;

    private String reviewer;

    private Integer reviewerId;

    private DateTime time;
    private LocalDateTime auditTime;

    private String academy;

    private String period;

    private LocalDate date;



    public SiteApplication(Integer id, ApplicationState status, String appAdvice, LocalDateTime auditTime) {
        this.id = id;
        this.status = status;
        this.appAdvice = appAdvice;
        this.auditTime = auditTime;
    }
}


