package cn.homyit.domain.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SiteType {
    @EnumValue
    LEV_1(1),
    @EnumValue
    LEV_2(2);
    int num;

    SiteType(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
