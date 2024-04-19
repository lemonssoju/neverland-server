package com.lesso.neverland.user.dto;

public record SignupRequest(String loginId,
                            String nickname,
                            String password,
                            String passwordCheck) {
}
