package top.ourlostyouth.www;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    @TokenNotValidation
    public String index() {
        return "auth-server服务启动成功";
    }

    @GetMapping("/1")
    @PreAuthorize("@el.check('12121')")
    @TokenNotValidation
    public String info1() {
        return "auth-server服务启动成功";
    }

    @GetMapping("/actuator/info")
    public String info() {
        return "auth-server服务启动成功";
    }
}
