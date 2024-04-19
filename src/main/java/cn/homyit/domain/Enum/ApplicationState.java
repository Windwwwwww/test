package cn.homyit.domain.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ApplicationState {
    ACCEPTED(0),
    AUDITING(1),
    REJECTED(2);
    @EnumValue
    private int num;

    ApplicationState(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
