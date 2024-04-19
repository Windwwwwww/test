package cn.homyit.dto;

import lombok.Data;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/28 16:48
 **/
@Data
public class SelectSiteDto {
    private String name;//申请位置
    private String applicant;//申请人
    private String time;//申请时间
    private String status;//申请状态
}
