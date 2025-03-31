package gateway.springcouldgateway.jwt;

/*
TokenProvider로 JWT 토큰을 만들거나 토큰을 바탕으로 유저 정보를 가져올 수 있다.
JwtFilter는 Request 앞단에 붙일 커스텀 필터로, 인증 처리를 담당한다.

토큰 생성 및 유효성 검사 | TokenProvider
유저의 인증 정보를 바탕으로 JWT 토큰을 생성하고, 이를 검증하는 역할을 한다.
즉, JWT 토큰 관련된 암호화, 복호화, 검증 로직이 모두 이 곳에서 이루어진다.
*/

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import gateway.springcouldgateway.jwt.dto.TokenDto;
import gateway.springcouldgateway.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Decoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {

    /*
토큰 생성 | generateTokenDto
• 인증된 사용자의 정보를 받아서 accessToken과 refreshToken 을 생성한다.
• 생성된 토큰들은 TokenDto 객체에 담겨 반환되고 유저는 유효한 토큰을 얻게 된다.

재발급 | reissueAccessToken
• 기존 refreshToken 을 검증하고 유효한 경우 새로운 accessToken 과 refreshToken 을 생성한다. → 이 때 accessToken 으로 토큰을 재발급하려는 시도가 보이면 예외를 던진다.
• 만약 refreshToken 이 만료되면 함께 생성하여 TokenDto 객체에 담겨 반환된다.

인증 객체 생성 | getAuthentication
• 스프링 시큐리티에서 사용할 수 있는 인증 객체를 생성하는 메소드이다.
• UserDetails 객체를 생성해서 UsernamePasswordAuthenticationToken 형태로 리턴하는데 SecurityContext를 사용하기 위한 절차이다.
• accessToken 을 통해 해당 토큰에 담긴 사용자 인증 정보를 Authentication 객체로 반환한다.

토큰 검증 | validateToken
• 토큰의 유효성을 검증한다.
    */

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    private final Algorithm algorithm;  // Algorithm으로 변경
    private final RedisUtil redisUtil;
    private final JWTVerifier verifier;
    @Value("${jwt.secret}")
    private String secretKey;

    public TokenProvider(@Value("${jwt.secret}")String secretKey, RedisUtil redisUtil) {

        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.algorithm = Algorithm.HMAC256(keyBytes);
        this.redisUtil = redisUtil;
        this.verifier = JWT.require(algorithm).build(); // ✅ JWT 검증기 생성
    }


/*
역할: 인증된 사용자의 정보를 바탕으로 액세스 토큰과 리프레시 토큰을 생성하고,
이를 TokenDto 객체에 담아 반환.

주요 점검 사항:
authorities를 쉼표로 구분하여 클레임에 넣음.
권한 정보가 정확히 들어가는지 확인 필요.
반환 값은 TokenDto로, 액세스 토큰과 리프레시 토큰이 포함됨.
*/
    public TokenDto generateTokenDto(Authentication authentication) {
        //권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = generateAccessToken(authentication.getName(), authorities);
        String refreshToken = generateRefreshToken(authentication.getName(),authorities);

        long now = (new Date()).getTime();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(new Date(now + ACCESS_TOKEN_EXPIRE_TIME).getTime())
                .refreshToken(refreshToken)
                .build();

    }


/*
역할: 리프레시 토큰을 검증하고
유효한 경우 새로운 액세스 토큰과 리프레시 토큰을 발급하여 반환.
주요 점검 사항:
parseJwt(refreshToken)을 통해 리프레시 토큰에서 사용자 정보를 추출하고,
검증된 후 새 토큰을 생성.
redisUtil.save(id, newRefreshToken)로 새로운 리프레시 토큰을 Redis에 저장.
*/
    public TokenDto reissueAccessToken(String refreshToken) {

        // 리프레시 토큰에서 사용자 정보 추출 -> 클레임 확인
        DecodedJWT jwt = parseJwt(refreshToken);

        validateRefreshToken(refreshToken);

        String id = jwt.getSubject();
        String authorities = jwt.getClaim(AUTHORITIES_KEY).toString();

        String newAccessToken = generateAccessToken(id, authorities);
        String newRefreshToken = generateRefreshToken(id, authorities);

        redisUtil.save(id, newRefreshToken);

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(newAccessToken)
                .accessTokenExpiresIn(new Date((new Date()).getTime() + ACCESS_TOKEN_EXPIRE_TIME).getTime())
                .refreshToken(newRefreshToken)
                .build();

    }

/*
역할: 액세스 토큰을 생성하고 반환.
주요 점검 사항:
HMAC256 알고리즘을 사용하여 서명.
withClaim()으로 권한 정보 포함.
*/
    private String generateAccessToken(String id, String authorities) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        return JWT.create()  // JWT 생성 시작
                .withSubject(id)  // subject로 id 설정
                .withClaim(AUTHORITIES_KEY, authorities)  // "AUTHORITIES_KEY"라는 클레임에 authorities 정보 설정
                .withExpiresAt(accessTokenExpiresIn)  // 만료 시간 설정
                .sign(Algorithm.HMAC256(secretKey.getBytes()));  // 비밀키로 서명 (HMAC512 사용)
    }

/*
역할: 리프레시 토큰을 생성하고 반환.
주요 점검 사항:
withClaim("isRefreshToken", true)로 리프레시 토큰임을 나타내는 클레임을 추가.
*/
    private String generateRefreshToken(String id, String authorities) {
        long now = System.currentTimeMillis();
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        return JWT.create()  // JWT 객체 생성
                .withSubject(id)  // subject에 email 설정
                .withClaim(AUTHORITIES_KEY, authorities)  // "AUTHORITIES_KEY"에 authorities 설정
                .withExpiresAt(refreshTokenExpiresIn)  // 만료 시간 설정
                .withClaim("isRefreshToken", true)  // 리프레시 토큰 여부를 나타내는 클레임 설정
                .sign(algorithm);  // HMAC 알고리즘으로 서명 (이전에 정의한 `algorithm` 사용)
    }

/*
역할: 액세스 토큰을 바탕으로 사용자 인증 정보를 추출하고, Authentication 객체를 반환.
주요 점검 사항:
authorities.split(",")로 권한을 분리하고,
SimpleGrantedAuthority로 변환하여 인증 객체를 만듦.
*/
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        DecodedJWT decodedJWT = parseJwt(accessToken);

        if (decodedJWT == null || decodedJWT.getClaim(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        String authorities = decodedJWT.getClaim(AUTHORITIES_KEY).asString();
        Collection<? extends GrantedAuthority> authoritiesList =
                Arrays.stream(authorities.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(decodedJWT.getSubject(), "", authoritiesList);

        return new UsernamePasswordAuthenticationToken(principal, "", authoritiesList);
    }

/*
역할: JWT 토큰을 검증.
주요 점검 사항:
JWTVerifier를 통해 검증.
*/
    public boolean validateToken(String token) {
        try {
            verifier.verify(token); // ✅ 토큰 검증
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public void validateRefreshToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }

        // ✅ 토큰의 "isRefreshToken" 클레임 확인
        DecodedJWT decodedJWT = JWT.decode(refreshToken);
        Boolean isRefreshToken = decodedJWT.getClaim("isRefreshToken").asBoolean();

        if (isRefreshToken == null || !isRefreshToken) {
            throw new RuntimeException("유효하지 않은 리프레시 토큰입니다.");
        }
    }

    private DecodedJWT parseJwt(String accessToken) {
        try {
            return JWT.require(Algorithm.HMAC256(secretKey)) // ✅ 서명 검증 추가
                    .build()
                    .verify(accessToken);
        } catch (Exception e) {
            return null;
        }
    }

}
