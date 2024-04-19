package cn.homyit.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchPageReq {
    private Integer pageNo;
    private Integer pageSize;
    private String label;

    private String name;
}
