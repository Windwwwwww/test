package cn.homyit.dto;

import lombok.Data;

@Data
public class GetBlogsDto {
    private int pageNum;
    private BlogDto blogDto;
}
