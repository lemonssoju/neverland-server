package com.lesso.neverland.user.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.dto.JwtDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

import static com.lesso.neverland.common.constants.Constants.INACTIVE;
import static com.lesso.neverland.common.constants.Constants.LOGOUT;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${jwt.access-token-validity-in-millis}")
    private int accessTokenExpirationTime;

    @Value("${jwt.refresh-token-validity-in-millis}")
    private int refreshTokenExpirationTime;

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final RedisService redisService;
    private final RedisTemplate<String, String> redisTemplate;


    // 토큰 발급
    public JwtDto generateToken(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return new JwtDto(accessToken, refreshToken);
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
        redisService.signup(refreshToken, user.getLoginId());
        return refreshToken;
    }

    // request에서 userIdx 추출
    public Long getUserIdx() throws BaseException {
        String token = getTokenFromRequest();
        if (token == null || token.equals("")) return null;
        return getUserIdxFromToken(token);
    }

    // 토큰에서 userIdx 추출
    public Long getUserIdxFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("userIdx", Long.class);
    }

    // 로그아웃
    public void logout(String refreshToken) throws BaseException {
        // 토큰 유효성 검사
        String accessToken = getTokenFromRequest();

        redisService.deleteFromRedis(refreshToken);
        redisService.registerBlackList(accessToken, LOGOUT);
    }

    // 회원 탈퇴
    public void signout(String refreshToken) throws BaseException {
        // 토큰 유효성 검사
        String accessToken = getTokenFromRequest();

        redisService.deleteFromRedis(refreshToken);
        redisService.registerBlackList(accessToken, INACTIVE);
    }

    public String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        else return null;
    }
}
