package com.sp.web.user.auth.model.dto;

import com.sp.web.user.auth.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private boolean success;   // 로그인 성공 여부
    private String accessToken;   // ✅ Access Token
    private String refreshToken;  // ✅ Refresh Token
    private String message;    // 메시지 (성공/실패 이유)
    private UserEntity user;

}
