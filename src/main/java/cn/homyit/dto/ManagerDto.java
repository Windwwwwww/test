package cn.homyit.dto;

import cn.homyit.dto.Enum.ManagerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//TODO 这个要删掉
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDto {
    private int userid;
    private ManagerType type;
    //3超级管理1一级审核2二级审核
}

