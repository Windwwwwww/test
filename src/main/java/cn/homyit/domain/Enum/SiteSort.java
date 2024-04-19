package cn.homyit.domain.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SiteSort {
    INDOOR(0), // 室内
    OUTDOOR(1); // 室外

    @EnumValue
    private final Integer num;

    SiteSort(int id) {
        this.num = id;
    }

    public Integer getNum() {
        return num;
    }
}
