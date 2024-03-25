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

import java.util.Optional;

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
    public JwtDto signup(SignupRequest signupRequest) {
        if(!signupRequest.password().equals(signupRequest.passwordCheck())) throw new BaseException(UNMATCHED_PASSWORD);

        User newUser = signupRequest.toUser(encoder.encode(signupRequest.password()));
        userRepository.save(newUser);

        for (SignupRequest.ContentsPreference contentsPreference : signupRequest.contentsPreferences()) {
            Contents contents = Contents.getEnumByName(contentsPreference.contents());
            Contents preference = Contents.getPreference(contents, contentsPreference.preference());
            interestService.createInterest(contents, preference, newUser);
        }
        return authService.generateToken(newUser.getUserIdx());
    }

    // 로그인
    @Transactional(rollbackFor = Exception.class)
    public JwtDto login(LoginRequest loginRequest) {
        User user = userRepository.findByLoginId(loginRequest.loginId()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        if(!encoder.matches(loginRequest.password(), user.getPassword())) throw new BaseException(WRONG_PASSWORD);

        JwtDto jwtDto = authService.generateToken(user.getUserIdx());

        user.login();
        userRepository.save(user);
        return jwtDto;
    }

    // 로그아웃
    @Transactional(rollbackFor = Exception.class)
    public void logout(Long userIdx) {
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        authService.logout(userIdx);

        user.logout();
        userRepository.save(user);
    }

    // 회원 탈퇴
    @Transactional(rollbackFor = Exception.class)
    public void signout(Long userIdx, SignoutRequest signoutRequest) {
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        if (!encoder.matches(signoutRequest.password(), user.getPassword())) throw new BaseException(WRONG_PASSWORD);
        authService.signout(userIdx);

        user.signout();
        userRepository.save(user);
    }

    // Access Token 재발급
    @Transactional(rollbackFor = Exception.class)
    public TokenResponse reissueAccessToken(ReissueTokenRequest reissueTokenRequest) {
        User user = userRepository.findByLoginIdAndStatusEquals(reissueTokenRequest.loginId(), ACTIVE).orElseThrow(() -> new BaseException(NO_MATCH_USER));
        validateRefreshToken(reissueTokenRequest, user.getUserIdx());

        // refresh token이 유효한 경우 access token 재발급
        return new TokenResponse(authService.generateAccessToken(user.getUserIdx()));
    }

    // refreshToken 유효성 체크
    private void validateRefreshToken(ReissueTokenRequest reissueTokenRequest, Long userIdx) {
        String refreshTokenFromRequest = reissueTokenRequest.refreshToken();
        if (refreshTokenFromRequest == null || refreshTokenFromRequest.isEmpty())
            throw new BaseException(INVALID_REFRESH_TOKEN);

        String refreshTokenFromRedis = redisService.getToken(userIdx);
        if(!refreshTokenFromRedis.equals(refreshTokenFromRequest)) throw new BaseException(INVALID_REFRESH_TOKEN);
    }

    // 닉네임 중복 체크
    public void validateNickname(String nickname) {
        if(userRepository.existsByProfile_Nickname(nickname)) throw new BaseException(DUPLICATED_NICKNAME);
    }

    // 아이디 중복 체크
    public void validateLoginId(String loginId) {
        if(userRepository.existsByLoginId(loginId)) throw new BaseException(DUPLICATED_LOGIN_ID);
    }

    // 개인 정보 변경
    @Transactional(rollbackFor = Exception.class)
    public void modifyUser(Long userIdx, ModifyUserRequest modifyUserRequest) {
        User user = userRepository.findByUserIdxAndStatusEquals(userIdx, ACTIVE).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        if(!encoder.matches(modifyUserRequest.password(), user.getPassword())) throw new BaseException(WRONG_PASSWORD);
        if (modifyUserRequest.newPassword().equals("") || modifyUserRequest.newPassword().equals(" "))
            throw new BaseException(INVALID_PASSWORD);
        user.modifyPassword(modifyUserRequest.newPassword());
        userRepository.save(user);
    }

    // 회원 validation
    public Long getUserIdxWithValidation() {
        Long userIdx = authService.getUserIdx();
        if (userIdx == null) throw new BaseException(NULL_ACCESS_TOKEN);
        return userIdx;
    }

    public User getUserByUserIdx(Long userIdx) {
        if(userIdx == null) return null;
        else {
            Optional<User> user = userRepository.findByUserIdx(userIdx);
            return user.orElse(null);
        }
    }
}
