package com.sp.web.user.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReissueResponseDto {

    private boolean success;
    private String accessToken;
    private String refreshToken; // rotation 정책이면 필요
    private String userId;
    private String message;

}
