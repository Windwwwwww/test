package cn.homyit.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ProcessDataUtil {
    /**
     * 处理查询后需要返回的数据
     * @param data
     * @return 返回二维数组
     */
    public static String[][] returnData(String data){
        // 去除大括号和空格
        data = data.replaceAll("[\\[\\]\"“”]", ""); // 去除中括号
        data = data.replaceAll("\\s+", ""); // 去除空格

        // 按逗号分割每个数据项
        String[] items = data.split("},");


        // 创建二维数组
        String[][] dataArray = new String[items.length][];
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            item = item.substring(1); // 去除每个数据项前后的大括号
            String[] keyValuePairs = item.split(",");
            int j = 0;
            int len = keyValuePairs.length;
            keyValuePairs[len- 1] = keyValuePairs[len - 1].replaceAll("\\}","");
            for (j = 0; j < len; j++) {
                String temp = keyValuePairs[j];
                boolean matches = Pattern.matches("^[\\u4e00-\\u9fa5]+=([0-9]+\\.[0-9]+)E(\\d+)$", temp);
//                boolean matches = Pattern.matches(".*\\d+(\\.\\d+)?[Ee][+-]?\\d+.*", temp);
//                if(Pattern.matches("\\}",))
                if (matches){
                    String[] split = temp.split("=");
//                    System.out.println("split = " + split[1]);
                    BigDecimal bigDecimal = new BigDecimal(split[1]);
                    String normalFormat = bigDecimal.toPlainString();
//                    System.out.println("normalFormat = " + normalFormat);
                    keyValuePairs[j] = split[0] + "=" + normalFormat;
                }
            }
            keyValuePairs[len- 1] = keyValuePairs[len - 1].replaceAll("\\}","");
            dataArray[i] = keyValuePairs;
        }
        return dataArray;
    }

    public static String[] stringToList(String str){
        String s = str.replaceAll("[，,]", ",");
//        System.out.println("s = " + s);
        return s.split(",");
    }

    public static String getFileName(String url){
        String[] split = url.split("/");
        return split[split.length - 1];
    }
}
