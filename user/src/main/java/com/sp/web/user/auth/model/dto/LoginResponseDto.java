package com.sp.web.user.auth.model.dto;

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
    private String userId;     // 로그인한 유저 ID
    private String message;    // 메시지 (성공/실패 이유)

}
