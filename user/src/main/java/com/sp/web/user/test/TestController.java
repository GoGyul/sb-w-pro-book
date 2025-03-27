package com.sp.web.user.test;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/user")
public class TestController {

    @GetMapping("")
    public void sample() {
        log.info("USER_TEST");
    }

}
