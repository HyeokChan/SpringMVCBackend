package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController : 문자를 반환하면 뷰이름으로 인식해서 찾는게 아니고 그대로 문자를 반환한다.
@Slf4j
@RestController
public class LogTestController {
    @GetMapping("/log-test")
    public String logTest(){
        String name = "Spring";
        log.trace("trace log={}", name);
        log.debug("debug log={}", name);
        log.info("info log={}", name);
        log.warn("warn log={}", name);
        log.error("error log={}", name);

        return "ok";
    }
}
