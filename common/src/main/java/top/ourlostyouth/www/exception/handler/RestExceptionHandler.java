package top.ourlostyouth.www.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ourlostyouth.www.exception.BadRequestException;
import top.ourlostyouth.www.response.Result;
import top.ourlostyouth.www.response.ResultGenerator;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author wudanjun
 * @Description：
 * @Date: Create in 10:24 2018/5/16
 * @Modified By:
 */
@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    /**
     * 远程服务异常消息
     *
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public Result<String> handleBusinessException(HttpServletRequest request, BadRequestException ex) {
        ex.printStackTrace();
        return ResultGenerator.fail(ex.getStatus(), ex.getMessage());
    }

}
