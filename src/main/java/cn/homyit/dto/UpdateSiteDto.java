package cn.homyit.dto;

import cn.homyit.domain.Enum.SiteType;
import lombok.Data;

@Data
public class UpdateSiteDto {

    private int id;
    private SiteType type;


}

