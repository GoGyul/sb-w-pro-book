package gateway.springcouldgateway.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}") // milliseconds
    private long expirationTime;

    public String createToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return JWT.create()
                .withSubject("AccessToken")
                .withClaim("userId",userId)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public boolean validateToken(String token) {
        try{
            JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
            return true;
        }catch (JWTVerificationException e){
            return false;
        }
    }

    public String getUserId(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
        return decodedJWT.getClaim("userId").asString();
    }

}
