package top.ourlostyouth.www;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    @TokenNotValidation
    public String index() {
        return "system-server服务启动成功";
    }

    @GetMapping("/actuator/info")
    public String info() {
        return "system-server服务启动成功";
    }
}
