package cn.homyit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/15 19:19
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class passwordDto {
    private String account;
    private String password;
}
