package com.sp.web.user.login.controller;

import com.sp.web.user.login.model.CreateUserDto;
import com.sp.web.user.login.service.LoginService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/create")
    private boolean createUser (@RequestBody CreateUserDto dto){

        return loginService.createUser(dto);

    }


}
