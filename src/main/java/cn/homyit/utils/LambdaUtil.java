package cn.homyit.utils;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.lang.reflect.Method;

public class LambdaUtil {
    public static <T> SFunction<T, ?> getFieldLambda(Class<T> clazz, String fieldName) {
        // 根据fieldName构建getter方法名，例如 "name" -> "getName"
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            // 获取getter方法的Method对象
            Method method = clazz.getMethod(getterName);
            // 构建并返回Lambda表达式
            return entity -> {
                try {
                    return method.invoke(entity);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
