package com.sp.web.user.redis.dto;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo implements Serializable {

    private String userId;
    private String refreshToken; // access or refresh
    private Instant createdAt = Instant.now();

    public TokenInfo(String userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.createdAt = Instant.now();
    }

}
