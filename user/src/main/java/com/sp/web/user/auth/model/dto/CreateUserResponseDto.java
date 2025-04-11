package com.sp.web.user.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserResponseDto {

    private boolean success;  // 회원가입 성공 여부
    private String userId;    // 생성된 사용자 ID (필요하다면)
    private String message;

}
