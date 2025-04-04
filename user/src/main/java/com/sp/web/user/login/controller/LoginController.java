package com.sp.web.user.login.controller;

import com.sp.web.user.login.model.dto.CreateUserDto;
import com.sp.web.user.login.model.dto.LoginUserDto;
import com.sp.web.user.login.service.LoginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/create")
    private boolean createUser (@RequestBody CreateUserDto dto){

        return loginService.createUser(dto);

    }

    @PostMapping("/public/login")
    private boolean login (@RequestBody LoginUserDto dto){

        log.info("🔑 Login attempt: {}", dto.getUserId());

        Boolean result = loginService.loginService(dto);

        return result;

    }


}
