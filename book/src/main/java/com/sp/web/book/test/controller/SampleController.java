package com.sp.web.book.test.controller;

import com.sp.web.book.test.dto.SampleDto;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/sample")
public class SampleController {

    @RequestMapping("")
    public void sample() {
        log.info("sample");
    }

    @GetMapping("/ex01")
    public SampleDto ex01() {
        SampleDto dto = new SampleDto();
        dto.setId("1");
        dto.setName("name");
        return dto;
    }

    @GetMapping("/ex02")
    public SampleDto ex02(SampleDto inDto) {
        SampleDto dto = new SampleDto();
        dto.setId("2");
        dto.setName("inDto");
        return dto;
    }

}
