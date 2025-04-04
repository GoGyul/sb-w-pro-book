package gateway.springcouldgateway.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import gateway.springcouldgateway.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    public String createToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

        return JWT.create()
                .withSubject("AccessToken")
                .withClaim("userId",userId)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC256(jwtConfig.getSecret()));
    }

    public boolean validateToken(String token) {
        try{
            JWT.require(Algorithm.HMAC256(jwtConfig.getSecret())).build().verify(token);
            return true;
        }catch (JWTVerificationException e){
            return false;
        }
    }

    public String getUserId(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(jwtConfig.getSecret()))
                .build()
                .verify(token);
        return decodedJWT.getClaim("userId").asString();
    }

}
