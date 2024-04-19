package cn.homyit.domain.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum SiteState {
    @EnumValue
    OCCUPIED(0),
    @EnumValue
    UNOCCUPIED(1);
    int num;

    SiteState(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
