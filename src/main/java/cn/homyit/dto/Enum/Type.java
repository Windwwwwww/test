package cn.homyit.dto.Enum;

public enum Type {
    SITE(0),
    CLUB(1);
    private int num;

    Type(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }
}
