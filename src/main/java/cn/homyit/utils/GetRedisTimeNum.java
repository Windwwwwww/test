package cn.homyit.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class GetRedisTimeNum {

    public static  Map<String,Long> getRedisNum(LocalDate othertime, String period){
        String[] s=period.split("_");
        LocalTime begin=LocalTime.parse(s[0]);
        LocalTime end=LocalTime.parse(s[1]);
        LocalDate today=LocalDate.now();
        long days= ChronoUnit.DAYS.between(today,othertime)-2;
        long begintime=ChronoUnit.HOURS.between(LocalTime.parse("08:00"),begin)+1+days*14;
        long endtime=ChronoUnit.HOURS.between(LocalTime.parse("08:00"),end)+1+days*14;
        Map<String,Long> map=new HashMap<>();
        map.put("begintime",begintime);
        map.put("endtime",endtime);
        return map;

    }
}
