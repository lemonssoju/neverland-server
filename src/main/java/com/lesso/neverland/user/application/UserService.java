package com.lesso.neverland.user.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.dto.SignupRequest;
import com.lesso.neverland.user.dto.SignupResponse;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lesso.neverland.common.BaseResponseStatus.DATABASE_ERROR;
import static com.lesso.neverland.common.BaseResponseStatus.UNMATCHED_PASSWORD;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder encoder;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public SignupResponse signup(SignupRequest signupRequest) throws BaseException {
        try {
            if(!signupRequest.password().equals(signupRequest.passwordCheck())) throw new BaseException(UNMATCHED_PASSWORD);

            User newUser = signupRequest.toUser(encoder.encode(signupRequest.password()));
            userRepository.save(newUser);
            return authService.generateToken(newUser);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
