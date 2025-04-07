package com.sp.web.user.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LogoutResponseDto {
    private boolean success;
    private String message;
    private String userId;  // 로그아웃된 사용자 ID (에러일 경우 null)
}
