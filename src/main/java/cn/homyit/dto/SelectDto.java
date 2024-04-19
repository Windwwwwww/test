package cn.homyit.dto;

import lombok.Data;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/28 16:51
 **/
@Data
public class SelectDto {

    private String clubName;//申请社团名称
    private String applicant;//社长
    private String picture;//社团图标
    private String time;//申请时间
    private String status;//申请状态
}
