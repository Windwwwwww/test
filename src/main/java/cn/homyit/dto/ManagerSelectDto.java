package cn.homyit.dto;

import cn.homyit.dto.Enum.ManagerType;
import lombok.Data;

@Data
public class ManagerSelectDto {

    private Integer id;
    private String userName;
    private String workplace;
    private String type;

}
