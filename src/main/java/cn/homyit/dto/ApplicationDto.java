package cn.homyit.dto;

import cn.homyit.domain.Enum.ApplicationState;
import cn.homyit.dto.Enum.Type;
import lombok.Data;

@Data
public class ApplicationDto {
    private String appAdvice;
    private ApplicationState applicationState;
    private Type type;//0场地1社团

    private int appId;

}

