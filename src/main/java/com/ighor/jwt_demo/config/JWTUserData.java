package com.ighor.jwt_demo.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email) {
}