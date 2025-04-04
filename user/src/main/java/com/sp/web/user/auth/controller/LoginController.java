package com.sp.web.user.auth.controller;

import com.sp.web.user.auth.model.dto.CreateUserDto;
import com.sp.web.user.auth.model.dto.LoginResponseDto;
import com.sp.web.user.auth.model.dto.LoginUserDto;
import com.sp.web.user.auth.service.LoginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/public/create")
    private boolean createUser (@RequestBody CreateUserDto dto){

        return loginService.createUser(dto);

    }

    @PostMapping("/public/login")
    private LoginResponseDto login (@RequestBody LoginUserDto dto){

        log.info("🔑 Login attempt: {}", dto.getUserId());

        String token = loginService.postLogin(dto);

        return new LoginResponseDto(
                true,       // ✅ 로그인 성공 여부
                token,      // ✅ JWT 토큰
                dto.getUserId(),
                "로그인 성공"
        );

    }


}
