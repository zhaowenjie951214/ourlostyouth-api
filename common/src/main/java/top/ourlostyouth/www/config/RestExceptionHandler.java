package top.ourlostyouth.www.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ourlostyouth.www.exception.BadRequestException;

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
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Object handleBusinessException(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        return ex.getMessage();
    }

}
