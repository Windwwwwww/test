package cn.homyit.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JustifySiteDto {

    private String siteName;
    private String period;

    private LocalDate date;


}
