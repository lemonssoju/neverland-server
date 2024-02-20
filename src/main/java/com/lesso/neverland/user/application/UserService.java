package com.lesso.neverland.user.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.interest.application.InterestService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.dto.*;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lesso.neverland.common.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final InterestService interestService;
    private final RedisService redisService;

    // 회원가입
    @Transactional(rollbackFor = Exception.class)
    public JwtDto signup(SignupRequest signupRequest) throws BaseException {
        try {
            if(!signupRequest.password().equals(signupRequest.passwordCheck())) throw new BaseException(UNMATCHED_PASSWORD);

            User newUser = signupRequest.toUser(encoder.encode(signupRequest.password()));

            for (SignupRequest.ContentsPreference contentsPreference : signupRequest.contentsPreferences()) {
                Contents contents = Contents.getEnumByName(contentsPreference.contents());
                Contents preference = Contents.getPreference(contents, contentsPreference.preference());
                interestService.createInterest(contents, preference, newUser);
            }
            userRepository.save(newUser);
            return authService.generateToken(newUser);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 로그인
    @Transactional(rollbackFor = Exception.class)
    public JwtDto login(LoginRequest loginRequest) throws BaseException {
        try {
            User user = userRepository.findByLoginId(loginRequest.loginId()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            if(!encoder.matches(loginRequest.password(), user.getPassword())) throw new BaseException(INVALID_PASSWORD);

            JwtDto jwtDto = authService.generateToken(user);

            user.login();
            userRepository.save(user);
            return jwtDto;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 로그아웃
    @Transactional(rollbackFor = Exception.class)
    public void logout(Long userIdx, String refreshToken ) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            authService.logout(refreshToken);

            user.logout();
            userRepository.save(user);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 회원 탈퇴
    @Transactional(rollbackFor = Exception.class)
    public void signout(Long userIdx, String refreshToken) throws BaseException {
        try {
            User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            authService.signout(refreshToken);

            user.signout();
            userRepository.save(user);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // Access Token 재발급
    @Transactional(rollbackFor = Exception.class)
    public TokenResponse reissueAccessToken(ReissueTokenRequest reissueTokenRequest) throws BaseException {
        try {
            validateRefreshToken(reissueTokenRequest);
            String loginId = redisService.getLoginIdFromRedis(reissueTokenRequest.refreshToken());
            User user = userRepository.findByLoginIdAndStatusEquals(loginId, ACTIVE).orElseThrow(() -> new BaseException(NO_MATCH_USER));

            // refresh token이 만료되지 않은 경우 access token 재발급
            if (redisService.checkExistsRedis(reissueTokenRequest.refreshToken())) {
                return new TokenResponse(authService.generateAccessToken(user));
            } else throw new BaseException(INVALID_REFRESH_TOKEN);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // refreshToken 유효성 체크
    private void validateRefreshToken(ReissueTokenRequest reissueTokenRequest) throws BaseException {
        try {
            String refreshTokenFromRequest = reissueTokenRequest.refreshToken();
            if (refreshTokenFromRequest == null || refreshTokenFromRequest.isEmpty())
                throw new BaseException(INVALID_REFRESH_TOKEN);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 닉네임 중복 체크
    public void validateNickname(String nickname) throws BaseException {
        if(userRepository.existsByProfile_Nickname(nickname)) throw new BaseException(DUPLICATED_NICKNAME);
    }
}
