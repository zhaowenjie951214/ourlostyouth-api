package top.ourlostyouth.www.response;

import java.util.List;

/**
 * 响应结果生成工具
 * @author zhaowenjie
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    /**
     * 成功 返回自定义状态码与提示语
     *
     * @return
     */
    public static <T> Result<T> success(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }


    /**
     * 返回成功,不带数据
     *
     * @return
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(DEFAULT_SUCCESS_MESSAGE);
        return result;
    }

    /**
     * 返回成功，带数据
     *
     * @param data
     * @return
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    /**
     * 返回成功，带数据
     *
     * @param message
     * @param data
     * @return
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 返回分页查询的对象
     *
     * @param rows
     * @param total
     * @return
     */
    public static Result returnPage(List rows, Long total) {
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS.getCode());
        ResponsePage responsePage = new ResponsePage();
        responsePage.setRows(rows);
        responsePage.setTotal(total);
        result.setData(responsePage);
        return result;
    }

    /**
     * 返回失败及错误信息
     *
     * @param message
     * @return
     */
    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.FAIL.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 返回失败及错误信息
     *
     * @param message
     * @return
     */
    public static <T> Result<T> fail(String message, T data) {
        Result<T> result = new Result<T>();
        result.setCode(ResultCode.FAIL.getCode());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * @param code    错误代码
     * @param message 错误信息
     * @return
     */
    public static  <T> Result<T> fail(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 返回错误，带数据,自定义Code,消息
     *
     * @param data xu yai
     * @return
     */
    public static  <T> Result<T> message(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

}
