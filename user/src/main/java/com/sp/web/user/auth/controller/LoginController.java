package com.sp.web.user.auth.controller;

import com.sp.web.user.auth.model.dto.*;
import com.sp.web.user.auth.service.LoginService;
import com.sp.web.user.jwt.JwtUtil;
import com.sp.web.user.redis.dto.TokenInfo;
import com.sp.web.user.redis.service.RedisLoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.parser.Token;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final RedisLoginService redisLoginService;
    private final JwtUtil jwtUtil;

    @PostMapping("/public/create")
    private boolean createUser (@RequestBody CreateUserDto dto){

        return loginService.createUser(dto);

    }

    @PostMapping("/public/login")
    private LoginResponseDto login (@RequestBody LoginUserDto dto){

        log.info("🔑 Login attempt: {}", dto.getUserId());

        // accessToken과 refreshToken 모두 발급
        var tokenMap = loginService.postLogin(dto);

        return new LoginResponseDto(
                true,
                tokenMap.get("accessToken"),
                tokenMap.get("refreshToken"),
                dto.getUserId(),
                "로그인 성공"
        );

    }

    @PostMapping("/public/logout")
    private LogoutResponseDto logout(HttpServletRequest request){

        return loginService.postLogout(request);

    }

    @PostMapping("/public/reissue")
    private ReissueResponseDto reissue (@RequestHeader("Authorization") String refreshTokenHeader){

        return loginService.postReissue(refreshTokenHeader);

    }


}
