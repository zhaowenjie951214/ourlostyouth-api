package top.ourlostyouth.www;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "auth-server服务启动成功";
    }

    @GetMapping("/actuator/info")
    @PreAuthorize("@el.check('app:add')")
    public String info() {
        return "auth-server服务启动成功";
    }
}
