package com.lesso.neverland.user.application;

import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.dto.SignupResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${jwt.access-token-validity-in-millis}")
    private int accessTokenExpirationTime;

    @Value("${jwt.refresh-token-validity-in-millis}")
    private int refreshTokenExpirationTime;
    @Value("${jwt.secret-key}")
    private String secretKey;

    private final RedisTemplate<String, String> redisTemplate;

    // 토큰 발급
    public SignupResponse generateToken(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return new SignupResponse(accessToken, refreshToken);
    }

    // accessToken 발급
    public String generateAccessToken(User user) {
        Claims claims = Jwts.claims();
        claims.put("loginId", user.getLoginId());
        claims.put("userIdx", user.getUserIdx());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // refreshToken 발급
    public String generateRefreshToken(User user) {
        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        redisTemplate.opsForValue().set(refreshToken, user.getLoginId(), Duration.ofMillis(refreshTokenExpirationTime));
        return refreshToken;
    }
}
