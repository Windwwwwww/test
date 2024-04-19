package cn.homyit.domain;

import cn.homyit.domain.Enum.ApplicationState;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@TableName("club_application")
@AllArgsConstructor
@NoArgsConstructor
public class ClubApplication {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private ApplicationState status;

    private String appAdvice;
    //审批意见
    private String clubName;
    private String introduction;
    private String reviewer;

    private String reviewerId;
    @TableField(fill= FieldFill.INSERT)
    private Integer applicantId;
    //申请人id
    @TableField(fill=FieldFill.INSERT)
    private String applicant;

    private LocalDateTime time;
    private LocalDateTime auditTime;

    private String picture;


    public ClubApplication(Integer id, ApplicationState status, String appAdvice, LocalDateTime auditTime) {
        this.id = id;
        this.status = status;
        this.appAdvice = appAdvice;
        this.auditTime = auditTime;
    }
}
