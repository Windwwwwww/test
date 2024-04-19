package cn.homyit.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

public class CreateWrapper {

    private static final Map<Class<?>, Map<String, SFunction<?, ?>>> cache = new HashMap<>();

    public static <T, E> LambdaQueryWrapper<T> createWrapper(E entity, Class<T> targetClass) {
        LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
        Field[] fields = entity.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value != null && !(value instanceof Number && ((Number) value).doubleValue() == 0)) {
                    SFunction<T, ?> getter = getGetterFunction(targetClass, field.getName());
                    if (getter != null) {
                        queryWrapper.eq(getter, value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return queryWrapper;
    }

    @SuppressWarnings("unchecked")
    private static <T> SFunction<T, ?> getGetterFunction(Class<T> targetClass, String fieldName) {
        Map<String, SFunction<T, ?>> fieldGetterMap = (Map<String, SFunction<T, ?>>) (Map<?, ?>) cache.computeIfAbsent(targetClass, k -> createGetterMap(targetClass));
        return fieldGetterMap.get(fieldName);
    }

    private static <T> Map<String, SFunction<?, ?>> createGetterMap(Class<T> targetClass) {
        Map<String, SFunction<?, ?>> getterMap = new HashMap<>();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                Method getterMethod = targetClass.getMethod(getterName);
                SFunction<T, Object> getter = (T instance) -> {
                    try {
                        return getterMethod.invoke(instance);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };
                getterMap.put(fieldName, getter);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return getterMap;
    }
}
