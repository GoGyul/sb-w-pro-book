package gateway.springcouldgateway.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "MemberToken", timeToLive = 3600*24*14)
@Getter
@Setter
@AllArgsConstructor
public class Redis {

    @Id
    private String userId;
    private String refreshToken;

}
