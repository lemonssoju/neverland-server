package com.lesso.neverland.user.dto;

public record ReissueTokenRequest(String loginId, String refreshToken) {
}
