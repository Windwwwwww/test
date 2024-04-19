package cn.homyit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Integer code;
    private String message;
    private Object data;

    public Result(Object data) {
        this(200,data);
    }

    public Result(Integer code, Object data) {
        this.code = code;
        this.data = message;
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result AddOk(Object msg) {
//        return new Result(Code.ADD_OK.getStateNum(), "添加成功", msg);
        return new Result(200, "添加成功", msg);
    }

    public static Result success(){
        Result result = new Result();
        result.setCode(200);
        return result;
    }

    //返回状态码200和提示信息
    public static Result success(String message){
        Result result = new Result();
        result.setCode(200);
        result.setMessage(message);
        return result;
    }

    //返回状态码200和相关信息
    public static Result success(Object data){
        Result result = new Result();
        result.setCode(200);
        result.setData(data);
        return result;
    }

    //返回自定定义的错误状态码，和相关信息
    public static Result fail(Integer code, String message){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static Result error(String message) { return new Result(400,message,null);}
    public static Result ok(){return new Result(200,null,null);}
    public static Result ok(String msg) {
        return new Result(200,msg,null);
    }

    public static Result ok(String msg,Object o) {
        return new Result(200,msg,o);
    }


}
