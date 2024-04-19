package cn.homyit.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/17 14:11
 **/
public class StringToListUtils {
    public static List<String> stringToList(String string){
        String s = string.replaceAll("[，,]", ",");
//        System.out.println("s = " + s);
        return Arrays.asList(s.split(","));
    }
}
