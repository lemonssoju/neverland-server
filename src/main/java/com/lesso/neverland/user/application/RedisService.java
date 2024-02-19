package com.lesso.neverland.user.application;

import com.lesso.neverland.common.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.lesso.neverland.common.BaseResponseStatus.EXPIRED_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class RedisService {

    @Value("${jwt.refresh-token-validity-in-millis}")
    private int refreshTokenExpirationTime;
    private final RedisTemplate<String, String> redisTemplate;

    public void signup(String refreshToken, String loginId) {
        redisTemplate.opsForValue().set(refreshToken, loginId, Duration.ofMillis(refreshTokenExpirationTime));
    }

    public boolean checkExistsRedis(String refreshToken) throws BaseException {
        if (redisTemplate.opsForValue().get(refreshToken) != null) return true;
        else throw new BaseException(EXPIRED_REFRESH_TOKEN);
    }
}
