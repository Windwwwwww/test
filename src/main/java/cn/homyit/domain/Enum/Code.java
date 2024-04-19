package cn.homyit.domain.Enum;

public enum Code {
        ADD_OK(204,"添加成功"),
        ADD_ERR(501,"添加失败"),
        UPDATE_OK(205,"更新成功"),
        UPDATE_ERR(502,"更新失败"),
        DELETE_OK(206,"删除成功"),
        DELETE_ERR(503,"删除失败"),
        SELETE_OK(207,"检索成功"),
        SYSTEM_ERR(505,"系统错误");
        private final int stateNum;

        private final String msg;
        Code(int stateNum,String msg){
            this.stateNum=stateNum;
            this.msg=msg;
        }

        public int getStateNum() {
            return stateNum;
        }

        public String getMsg() {
                return msg;
        }
}
