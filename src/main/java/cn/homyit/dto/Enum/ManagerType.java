package cn.homyit.dto.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ManagerType {
    @EnumValue
    SUPER(3),
    @EnumValue
    LEV_1(1),
    @EnumValue
    LEV_2(2);
    int num;

    ManagerType(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
