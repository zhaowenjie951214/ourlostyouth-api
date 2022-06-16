package top.ourlostyouth.www.response;

/**
 * 统一API响应结果封装
 */
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public Result(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.message = msg;
    }

    public Result() {

    }

    public Integer getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }


    public T getData() {
        return data;
    }

    public Result setCode(Integer code) {
        this.code = code;
        return this;
    }


    public Result setMessage(String message) {
        this.message = message;
        return this;
    }


    public Result setData(T data) {
        this.data = data;
        return this;
    }

}