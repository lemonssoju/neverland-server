package com.lesso.neverland.user.application;

import com.lesso.neverland.common.BaseException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

import static com.lesso.neverland.common.BaseResponseStatus.EXPIRED_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class RedisService {

    @Value("${jwt.refresh-token-validity-in-millis}")
    private int refreshTokenExpirationTime;

    @Value("${jwt.secret-key}")
    private String secretKey;
    private final RedisTemplate<String, String> redisTemplate;

    public void signup(String refreshToken, String loginId) {
        redisTemplate.opsForValue().set(refreshToken, loginId, Duration.ofMillis(refreshTokenExpirationTime));
    }

    public void deleteFromRedis(String refreshToken) {
        if(redisTemplate.opsForValue().get(refreshToken) != null) redisTemplate.delete(refreshToken);
    }

    public void registerBlackList(String accessToken, String status) {
        // accessToken expirationTime 동안 전달받은 staus 상태로 redis에 저장
        Long expirationTime = getExpirationTime(accessToken);
        redisTemplate.opsForValue().set(accessToken, status, Duration.ofMillis(expirationTime));
    }

    private Long getExpirationTime(String token) {
        Date accessTokenExpirationTime = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
        return accessTokenExpirationTime.getTime() - (new Date()).getTime();
    }

    public boolean checkExistsRedis(String refreshToken) throws BaseException {
        if (redisTemplate.opsForValue().get(refreshToken) != null) return true;
        else throw new BaseException(EXPIRED_REFRESH_TOKEN);
    }

    public String getLoginIdFromRedis(String refreshToken) {
        return redisTemplate.opsForValue().get(refreshToken);
    }
}
