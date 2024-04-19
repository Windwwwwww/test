package cn.homyit.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class PageReq {
    private Integer pageNo;
    private Integer pageSize;
}
