package cn.homyit.execption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SystemException extends RuntimeException{

    private int num;
    private String msg;

}
